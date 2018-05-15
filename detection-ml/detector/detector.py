from PIL import Image
from keras.models import model_from_json
import numpy as np

from sklearn import metrics
from sklearn.preprocessing import MinMaxScaler
from sklearn.cluster import KMeans
import datetime
import matplotlib.pyplot as plt
import random
import matplotlib.image as mpimg
import os
from scipy.spatial import distance_matrix


class Detector:
    def __init__(self, input_path='', output_path=''):
        self.input_path = input_path
        self.output_path = output_path
        self.w_width = 40
        self.w_height = 40
        self.stride = 10
        self.padding = 0
        self.threshold = 0.5

        # read the image
        self.image = Image.open(self.input_path)

        # set classifier paths
        self.classifier_model_path = 'detector/models/classifier/class_model_train_loss-0.00899300720441' \
                                     '_train_acc-0.990210576127_test_loss-0.151853362135_test_acc-' \
                                     '0.829227417503_162201811834.json'
        self.classifier_weights_path = 'detector/models/classifier/class_weights_train_loss-0.00899300720441' \
                                       '_train_acc-0.990210576127_0.1518533621350.82922741750316220181.h5'

        # set regressor paths
        self.regressor_model_path = 'detector/models/regressor/regress_model_train_loss-0.0157134450689' \
                                    '_train_acc-0.774896033606_test_loss-0.0371736100813_test_acc-' \
                                    '0.719717955175_162201824930.json'
        self.regressor_weights_path = 'detector/models/regressor/regress_weights_train_loss-0.0157134450689' \
                                      '_train_acc-0.774896033606_test_loss-0.0371736100813_test_acc-' \
                                      '0.719717955175_162201824930.h5'

        # load models

        # classifier
        classifier_file = open(self.classifier_model_path, 'r')
        self.classifier = model_from_json(classifier_file.read())
        self.classifier.load_weights(self.classifier_weights_path)

        # regressor
        regressor_file = open(self.regressor_model_path, 'r')
        self.regressor = model_from_json(regressor_file.read())
        self.regressor.load_weights(self.regressor_weights_path)

        # keep predictions
        self.predicted = None

    def recognize(self):

        begin = datetime.datetime.now()

        img = mpimg.imread(self.input_path)

        img_height = self.image.size[1]
        img_width = self.image.size[0]

        confidences = list()

        print("=> Sliding window, classification and regression")
        print()

        # perform sliding window
        for y in range(0, img_height - self.w_width, self.stride):
            for x in range(0, img_width - self.w_width, self.stride):

                # determine the width and height ends of the sub image
                _x = x + self.w_width
                _y = y + self.w_height

                # get the sub image
                template = self.image.crop((x, y, _x, _y))

                # convert the sub image to matrix
                array_template = np.array(template)
                r = array_template[:, :, 0]
                g = array_template[:, :, 1]
                b = array_template[:, :, 2]
                image_line = np.concatenate(
                    (np.reshape(r, r.size),
                     np.reshape(g, g.size),
                     np.reshape(b, b.size))
                )

                # normalization
                image_line = image_line / 255.

                # reshape with input shape of the models
                X = np.array([image_line])
                X = X.reshape((X.shape[0], self.w_width, self.w_height, 3))

                # classification
                pred = self.classifier.predict(X)
                pred = pred[0, 0]

                if pred >= self.threshold:
                    prediction = 1
                else:
                    prediction = 0

                if prediction == 1:

                    # predict coordinates
                    coordinates = self.regressor.predict(X)
                    coordinates *= self.w_height

                    if coordinates[0, 0] < 0:
                        coordinates[0, 0] = 0
                    if coordinates[0, 0] > self.w_height:
                        coordinates[0, 0] = self.w_height
                    if coordinates[0, 1] < 0:
                        coordinates[0, 1] = 0
                    if coordinates[0, 1] > self.w_height:
                        coordinates[0, 1] = self.w_height

                    prediction = list()
                    prediction.append(round(pred, 2))
                    prediction.append(x)
                    prediction.append(y)
                    prediction.append(int(coordinates[0, 0]))
                    prediction.append(int(coordinates[0, 1]))

                    confidences.append(prediction)

        confidences = np.array(confidences)

        if confidences.size == 0:
            img = mpimg.imread(self.input_path)
            plt.imshow(img)
            print('Number of tree = 0')
            plt.show()
        else:

            print("=> Clustering")
            print()

            # compute total number of windows
            total_windows = get_total_template_sliding_windows(img, self.w_width, self.stride)

            # compute the percentage of the covered zone
            percentage_covered = confidences.shape[0] / total_windows
            print('Estimation of the covered field: {}%'.format(round(100 * percentage_covered, 2)))
            print()

            X = confidences[:, 3:]
            template_pos = confidences[:, 1:3]
            X[:, 0] = X[:, 0] + template_pos[:, 0]
            X[:, 1] = X[:, 1] + template_pos[:, 1]

            # Clustering
            x_copy = X
            max_param = clustering_kmeans(x_copy, img, self.stride, percentage_covered, scale=False)

            # end
            end = datetime.datetime.now()

            print()
            print('Total time: {} seconds'.format(int((end - begin).total_seconds())))

            img = mpimg.imread(self.input_path)
            plt.imshow(img)

            x = max_param['model'].cluster_centers_[:, 0]
            y = max_param['model'].cluster_centers_[:, 1]

            # save the prediction

            self.predicted = np.rint(max_param['model'].cluster_centers_)

            print('Number of tree = {}'.format(max_param['n_core_samples']))

            plt.plot(x, y, 'r.', markersize=10)
            plt.savefig(
                os.path.join(self.output_path, 'detection_{}{}{}-{}{}{}_stride-{}_threshold-{}_padding-{}.jpeg'.format(
                    datetime.datetime.now().day,
                    datetime.datetime.now().month,
                    datetime.datetime.now().year,
                    datetime.datetime.now().hour,
                    datetime.datetime.now().minute,
                    datetime.datetime.now().second,
                    self.stride,
                    self.threshold,
                    self.padding)), dpi=1200)
            plt.show()

    def mapping(self, real=None):
        n = real.shape[0]
        m = self.predicted.shape[0]

        # fill the distance matrix
        D = distance_matrix(real, self.predicted)

        max_number = self.w_width * self.w_height

        if n < m:
            nb_points = n
        else:
            nb_points = m

        print(n, m, nb_points, np.max(D), np.min(D.min))

        selected_real = np.zeros((nb_points, 2))
        selected_predicted = np.zeros((nb_points, 2))

        distances = list()

        for i in range(nb_points):
            r, p = np.unravel_index(D.argmin(), D.shape)
            distances.append(D[r, p])
            D[r, :] = float('inf')
            D[:, p] = float('inf')
            selected_real[i, 0] = real[r, 0]
            selected_real[i, 1] = real[r, 1]
            selected_predicted[i, 0] = self.predicted[p, 0]
            selected_predicted[i, 1] = self.predicted[p, 1]

        print(distances)

        # average distance error
        ade = np.array(distances).sum() / nb_points

        print('Average distance error={}'.format(ade))

        img = mpimg.imread(self.input_path)
        plt.imshow(img)

        plt.plot(selected_real[:, 0], selected_real[:, 1], 'y.', markersize=7)
        plt.plot(selected_predicted[:, 0], selected_predicted[:, 1], 'r.', markersize=7)
        for i in range(nb_points):
            plt.plot([selected_real[i, 0], selected_predicted[i, 0]], [selected_real[i, 1], selected_predicted[i, 1]], color="blue", linewidth=1.0, linestyle="-", markersize=7)

        plt.savefig(
            os.path.join(self.output_path,
                         'detection_mapping_{}{}{}-{}{}{}_stride-{}_threshold-{}_padding-{}.jpg'.format(
                             datetime.datetime.now().day,
                             datetime.datetime.now().month,
                             datetime.datetime.now().year,
                             datetime.datetime.now().hour,
                             datetime.datetime.now().minute,
                             datetime.datetime.now().second,
                             self.stride,
                             self.threshold,
                             self.padding)), dpi=900)

        # plot distances

        plt.show()
        plt.close()

        distances = np.linalg.norm(selected_real - selected_predicted, axis=1)

        plt.plot(range(distances.size), distances, color="blue", linewidth=1.0, linestyle="-", markersize=7)
        plt.plot(int(distances.size) / 2, ade, 'g.', markersize=7)
        plt.savefig(
            os.path.join(self.output_path,
                         'detection_distances_{}{}{}-{}{}{}_stride-{}_threshold-{}_padding-{}.jpg'.format(
                             datetime.datetime.now().day,
                             datetime.datetime.now().month,
                             datetime.datetime.now().year,
                             datetime.datetime.now().hour,
                             datetime.datetime.now().minute,
                             datetime.datetime.now().second,
                             self.stride,
                             self.threshold,
                             self.padding)), dpi=900)
        plt.show()


def get_total_template_sliding_windows(image, width_template, stride):
    h = image.shape[0]
    w = image.shape[1]

    total = 0

    for y in range(0, h, stride):
        for x in range(0, w, stride):
            if (x + width_template) < w and (y + width_template) < h:
                total += 1

    return total


def clustering_kmeans(X, image, stride, covered_percentage, scale=True):
    max_param = dict()
    max_param['silhouette'] = None
    max_param['model'] = None
    max_param['n_core_samples'] = None

    parameters = list()

    h = image.shape[0]
    w = image.shape[1]

    n_w = int((w - 40) / stride)
    n_h = int((h - 40) / stride)

    # determine the range of center
    max_center = int((n_h * n_w) * covered_percentage / 4)
    min_center = int((n_h * n_w) * covered_percentage / 8)
    print('Range of centers: [{} - {}]'.format(min_center, max_center))
    print()

    if scale:
        min_max_scaler = MinMaxScaler()
        X = min_max_scaler.fit_transform(X)

    max_silhouette = -100

    for n_kernel in range(min_center, max_center):
        try:
            random.seed(1000)
            kmeans = KMeans(n_clusters=n_kernel, random_state=0, n_jobs=-1, max_iter=500).fit(X)

            parameter = list()
            labels = kmeans.labels_
            silhouette = metrics.silhouette_score(X, labels)

            if silhouette > max_silhouette:
                max_silhouette = silhouette
                print('Best silhouette: {}'.format(max_silhouette))
                print('Number of center: {}'.format(n_kernel))
                print()
                max_param['silhouette'] = silhouette
                max_param['model'] = kmeans
                max_param['n_core_samples'] = n_kernel
                if scale:
                    max_param['scaler'] = min_max_scaler
                else:
                    max_param['scaler'] = None
            parameter.append(n_kernel)
            parameter.append(silhouette)
            parameters.append(parameter)
        except:
            pass
    return max_param
