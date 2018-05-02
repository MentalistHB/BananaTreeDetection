from config import Config as cfg
from processing.image.processing import *
from processing.image.processing_roi import *
import pandas as pd
from scipy.spatial import distance_matrix, distance

# coordinates_data = pd.read_csv(cfg.coordinates_path, sep=',').loc[:, ['x', 'y']].values
# crop_image_from_coordinates(cfg.image_path, coordinates_data, cfg.original)

# groupe_image_files()
# extract_no_doublon_from_data()
# crop_images()
# draw_picked_zone()
# get_not_leaf_croped()
# get_sub_images()
# visual(cfg.image_path, coordinates_data)
# augmentation()

# origin = (3, 2)
# point = (4, 3)
#
# print(rotate_marked_point(origin, point, math.radians(270)))
# augmentation_marked_point()
# build_image_pixel_dataset()
# get_pixel()
# visualisation_transformation()

# import numpy as np
#
# a = np.array([[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]])
# #a = a.reshape((3, 2, 3, 2))
# c = a[0].reshape((3, 2, 2))
# print(c.transpose(1, 2, 0))
# print(a[0].reshape((2, 2, 3)))

# split_train_test()
# histogram()
# visualisation_used_data()
# conf_mat()
# visualize_predicted_points()
# mark_point()
# merge()
# visualize_green_level()

# real = np.array([[0, 0], [0, 1]])
# predicted = np.array([[4, 4], [1, 1], [0, 0]])

r = np.array([[1, 1],
              [2, 4],
              [5, 1],
              [6, 2]])

p = np.array([[1.5, 1],
              [1, 1.5],
              [4.5, 1.5]])


def mapping(real=None, predicted=None):
    n = real.shape[0]
    m = predicted.shape[0]

    # fill the distance matrix
    D = distance_matrix(real, predicted)

    max_number = 40 * 40

    if n < m:
        nb_points = n
    else:
        nb_points = m

    selected_real = np.zeros((nb_points, 2))
    selected_predicted = np.zeros((nb_points, 2))

    for i in range(nb_points):
        r, p = np.unravel_index(D.argmin(), D.shape)
        D[r, :] = max_number
        D[:, p] = max_number
        selected_real[i, 0] = real[r, 0]
        selected_real[i, 1] = real[r, 1]
        selected_predicted[i, 0] = predicted[p, 0]
        selected_predicted[i, 1] = predicted[p, 1]

    # average distance error
    ade = np.linalg.norm(selected_real - selected_predicted, axis=1).sum() / nb_points
    print(ade)
    print(selected_predicted)
    print(selected_real)


# mapping(r, p)
conf_mat()