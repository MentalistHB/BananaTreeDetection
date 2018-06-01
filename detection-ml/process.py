from sklearn.model_selection import StratifiedKFold
import os
import pandas as pd
import numpy as np
from config import Config as cfg
from sklearn.metrics import confusion_matrix
import datetime
from performanceModel import *
import random

class Process:
    def __init__(self, data_frame=None, dataFrameToPredict = None, learning_model=None, predicted_column='', cv_split=90, k_fold=5, id_length=50, excluded_columns=[], comment=''):
        self.data_frame = data_frame
        self.data_frame_to_predict = dataFrameToPredict
        self.k_fold = k_fold
        self.learning_model = learning_model
        self.id_length = id_length
        self.predicted_column = predicted_column
        self.predicted_index = self.data_frame.columns.get_loc(self.predicted_column)
        self.exclude_columns = excluded_columns
        self.cv_split = cv_split
        self.comment=comment

    def run(self):

        self.id = self.generate_id()

        self.data = self.read_data(self.data_frame)
        self.dataToPredict = self.read_data(self.data_frame_to_predict)

        self.run_cv(self.data, self.cv_split)
        self.build_model(self.data, self.dataToPredict)

    def read_data(self, data_frame):
        return data_frame.values

    def split_data(self, data, cv_split):

        split = int((np.size(data, axis=0) * cv_split) / 100)

        return data[:split, :], data[split:, :]

    def run_cv(self, data, cv_split):
        self.begin_cv = datetime.datetime.now()

        self.data_train, self.data_test = self.split_data(data, cv_split)
        self.apply_cv(self.k_fold, self.data_train)
        model_cv = self.build_model_cv()

        X_test, y_test = self.getXy(self.data_test, self.predicted_index)
        predicted_test = model_cv.predict(X_test)
        self.acc_test = self.accuracy(y_test, predicted_test)

        X, y = self.getXy(self.data, self.predicted_index)
        self.learning_model.fit(X, y)
        predicted_res = self.learning_model.predict(X)
        self.acc_res = self.accuracy(y, predicted_res)

        self.end_cv = datetime.datetime.now()

        self.performance = self.performance()
        self.write_performance(self.performance)

    def build_model(self, data, dataToPredict):

        X, y = self.getXy(data, self.predicted_index)
        self.learning_model.fit(X, y)

        prediction = self.learning_model.predict(dataToPredict)
        self.write_prediction(prediction, self.id)


    def apply_cv(self, k_fold, data):
        self.acc_cv_res = 0
        self.acc_cv_test = 0
        skf = StratifiedKFold(n_splits=k_fold)
        X, y = self.getXy(data, self.predicted_index)
        for train_index, test_index in skf.split(X, y):
            X_train, X_test = X[train_index], X[test_index]
            y_train, y_test = y[train_index], y[test_index]

            self.learning_model.fit(X_train, y_train)
            predicted_cv_test = self.learning_model.predict(X_test)
            predicted_cv_res = self.learning_model.predict(X_train)

            acc_test = self.accuracy(y_test, predicted_cv_test)
            acc_res = self.accuracy(y_train, predicted_cv_res)

            self.acc_cv_res += acc_res
            self.acc_cv_test += acc_test

        self.acc_cv_res /= self.k_fold
        self.acc_cv_test /= self.k_fold

    def build_model_cv(self):
        X, y = self.getXy(self.data_train, self.predicted_index)
        self.learning_model.fit(X, y)

        return self.learning_model

    def performance(self):
        _performance = list()
        _model = list()

        # Informationen der Datei performances.csv

        # Id
        _performance.append(self.id)

        # Datum
        _performance.append('{}.{}.{}'.format(self.begin_cv.day, self.begin_cv.month, self.begin_cv.year))

        # Uhrzeit
        _performance.append('{}:{}:{}'.format(self.begin_cv.hour, self.begin_cv.minute, self.begin_cv.second))
        end = self.end_cv

        # Laufzeit
        _performance.append((self.end_cv - self.begin_cv).total_seconds())

        # Anzahl_Merkmale
        _performance.append(np.size(self.data_train,
                                    axis=1) - 1)  # Die Anzahl der Merkmale ist die Anzahl der Spalten minus 1 aufgrund der Revenue-Spalte, die nicht als Merkmal gezählt ist.

        # Trainingsdaten
        _performance.append(np.size(self.data_train,
                                    axis=0))  # Die Anzahl der Merkmale ist die Anzahl der Spalten minus 1 aufgrund der Revenue-Spalte, die nicht als Merkmal gezählt ist.

        # Testdaten
        _performance.append(np.size(self.data_test,
                                    axis=0))  # Die Anzahl der Merkmale ist die Anzahl der Spalten minus 1 aufgrund der Revenue-Spalte, die nicht als Merkmal gezählt ist.

        # acc_cv_res
        _performance.append(self.acc_cv_res)

        # acc_cv_test
        _performance.append(self.acc_cv_test)

        # cv_mean
        _performance.append((self.acc_cv_res + self.acc_cv_test) / 2)

        # acc_res
        _performance.append(self.acc_res)

        # acc_test
        _performance.append(self.acc_test)

        # test_mean
        _performance.append((self.acc_res + self.acc_test) / 2)

        # comment
        _performance.append(self.comment)

        # Informationen der Datei models.csv

        # Id
        _model.append(self.id)

        # Modell
        _model.append(self.learning_model.__class__.__name__)  # Der Modellname wird als Zeichenkette entnommen

        # Merkmale
        _model.append(list(self.data_frame.columns))  # Der Modellname wird als Zeichenkette entnommen

        # cv_split
        _model.append(self.cv_split)

        # k-Fold
        _model.append(self.k_fold)

        # Parameter
        _model.append(self.learning_model.get_params())  # Alle Parameter werden als Zeichenkette entnommen

        _performance_array = np.array([_performance])
        _model_array = np.array([_model])

        pm = PerformanceModel(_performance_array, _model_array)

        return pm

    def write_performance(self, performance):
        if os.path.exists(
                cfg.performanceDataHome) == False:  # Falls der Ordner, wo die Ausgangsdatei liegt nicht vorhanden ist, dann wird er erstellt.
            os.makedirs(cfg.performanceDataHome)

        if not os.path.exists(cfg.modelDataModellierung):
            performDf = pd.DataFrame(performance.performance, columns=cfg.performance_header)
            performDf.to_csv(cfg.performanceDataModellierung, index=False, sep=cfg.sep)

            modelDf = pd.DataFrame(performance.model, columns=cfg.model_header)
            modelDf.to_csv(cfg.modelDataModellierung, index=False, sep=cfg.sep)
        else:
            performDf = pd.DataFrame(performance.performance)
            modelDf = pd.DataFrame(performance.model)

            performDf.to_csv(cfg.performanceDataModellierung, mode='a', index=False, header=False, sep=cfg.sep)
            modelDf.to_csv(cfg.modelDataModellierung, mode='a', index=False, header=False, sep=cfg.sep)

    def exclude_column(self, data, excluded_columns):
        data.drop(excluded_columns, axis=1, inplace=True, errors='ignore')
        return data

    def getXy(self, data, y_index):
        #Als y wird die gesamte Spalte -in unserem Fall 'revenue'- rausgenommen.
        y = data[:, y_index]

        #Als X wird die Matrix ohne Zielspalte gespeichert.
        X = np.concatenate((data[:, :y_index], data[:, y_index + 1:]), axis=1)

        return X, y

    def accuracy(self, y_true, y_pred):
        conf_mat = confusion_matrix(y_true, y_pred)
        n_col = np.size(conf_mat, axis=1)
        eye = np.eye(n_col)

        self.write_conf_mat(conf_mat, self.id)

        acc = np.sum((conf_mat * eye)) / np.sum(conf_mat)
        return acc

    def generate_id(self):
        ind = list()
        id=''

        for i in range(ord('0'), ord('9') + 1):
            ind.append(i)

        for i in range(ord('a'), ord('z') + 1):
            ind.append(i)

        for i in range(ord('A'), ord('Z') + 1):
            ind.append(i)

        for j in range(self.id_length):
            c = random.randint(0, len(ind) - 1)
            id +=chr(ind[c])

        return id

    def write_prediction(self, prediction, id):

        if os.path.exists(cfg.resultsDataHome) == False:  # Falls der Ordner, wo die Ausgangsdatei liegt nicht vorhanden ist, dann wird er erstellt.
            os.makedirs(cfg.resultsDataHome)

        predicted = np.array([prediction]).T

        predictedDf = pd.DataFrame(predicted, columns=cfg.result_header)
        predictedDf.to_csv(cfg.resultsDataResult.format(id), index=False, sep=cfg.sep)

    def write_conf_mat(self, conf_mat, id):

        if os.path.exists(cfg.confMatDataHome) == False:  # Falls der Ordner, wo die Ausgangsdatei liegt nicht vorhanden ist, dann wird er erstellt.
            os.makedirs(cfg.confMatDataHome)

        predictedDf = pd.DataFrame(conf_mat)
        predictedDf.to_csv(cfg.confMatDataResult.format(id), index=False, sep=cfg.sep)