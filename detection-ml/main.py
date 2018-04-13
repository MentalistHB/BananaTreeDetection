from config import Config as cfg
from processing.image.processing import *
from processing.image.processing_roi import *
import pandas as pd

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
merge()
# visualize_green_level()