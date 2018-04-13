from PIL import Image
import uuid
import os
import numpy as np
import pandas as pd
import matplotlib.image as mpimg
import matplotlib.pyplot as plt
import shutil
import math
import ntpath

from sklearn.metrics import confusion_matrix
from sklearn.cluster import DBSCAN
from sklearn import metrics
from sklearn.preprocessing import MinMaxScaler
from sklearn.cluster import KMeans
import datetime

from config import Config as cfg
import platform


def crop(image, x, y, width, height):
    """
    get a sub image from a given image
    :param image:
    :param x:
    :param y:
    :param width:
    :param height:
    :return:
    """
    sub_image = image.crop((x, y, width, height))
    return sub_image


def crop_image(image, stride, width, height):
    """
    Crop a given image with a stride of stride and return a list of sub images

    :param image:
    :param stride:
    :param width:
    :param height:
    :return: sub_images
    """
    # get size of the given image
    image_width, image_height = image.size

    # initialize the list of the resulting sub images
    sub_images = list()

    # go through the horizontal and vertical pixels
    for x_pos in range(0, image_width, stride):
        for y_pos in range(0, image_height, stride):
            sub_image_data = dict()
            # compute the new width and height
            sub_image_width = x_pos + width
            sub_image_height = y_pos + height

            # verify if the new sub image fit into the general one
            if sub_image_width < image_width and sub_image_height < image_height:
                # crop the image
                sub_image = crop(image, x_pos, y_pos, sub_image_width, sub_image_height)
                # insert the sub image into the list
                sub_image_data['id'] = str(uuid.uuid4())
                sub_image_data['x'] = x_pos
                sub_image_data['y'] = y_pos
                sub_image_data['width'] = width
                sub_image_data['height'] = height
                sub_image_data['stride'] = stride
                sub_image_data['widthparent'] = image_width
                sub_image_data['heightparent'] = image_height
                sub_image_data['image'] = sub_image
                sub_images.append(sub_image_data)

    return sub_images


def write_sub_images(sub_images, output):
    if (not os.path.exists(output)):
        os.makedirs(output)

    for sub_image in sub_images:
        sub_image["image"].save(
            "{}/{}_x-{}_y-{}_{}_by_{}_stride-{}_widthparent-{}_heightparent-{}.jpg"
                .format(
                output,
                sub_image['id'],
                sub_image['x'],
                sub_image['y'],
                sub_image['width'],
                sub_image['height'],
                sub_image['stride'],
                sub_image['widthparent'],
                sub_image['heightparent']
            )
        )


def crop_images():
    # input = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/P1000913.JPG"
    # output = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/croped"
    image_name = 'sub_image_x1-2540_y1-208_x2-3328_y2-496'
    input = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/roi_samples/processed/{}/{}.tif".format(
        image_name, image_name)
    output = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/roi_samples/processed/{}/croped".format(
        image_name)
    if (not os.path.exists(output)):
        os.makedirs(output)
    # all_files = os.walk(input)
    # for root, folders, images in all_files:
    #     for image_path in images:
    #         width = height = 40
    #         stride = int(width / 2)
    #         image = Image.open(os.path.join(input, image_path))
    #         sub_images = crop_image(image, stride, width, height)
    #         write_sub_images(sub_images, output)
    width = height = 40
    stride = int(width / 3)
    image = Image.open(input)
    sub_images = crop_image(image, stride, width, height)
    write_sub_images(sub_images, output)


def get_not_leaf_croped():
    input = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/P1000913.JPG"
    output = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/croped"
    output_non_green = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/non_green_windows"
    output_means = '/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/means.csv'
    width = height = 40
    stride = int(width / 3)
    image = Image.open(input)
    sub_images = crop_image(image, stride, width, height)

    means = list()
    non_greens = list()
    for i, sub_image in enumerate(sub_images):
        image_matrix = np.array(sub_image['image'])
        _mean = np.mean(image_matrix[:, :, 1])
        if _mean < 80:
            means.append(_mean)
            non_greens.append(sub_image)
        plt.plot(i, _mean, 'gs', markersize=2)
    plt.plot([0, len(sub_images)], [80, 80], 'r-', markersize=5)
    plt.show()

    # df = pd.DataFrame(means)
    # df.to_csv(output_means, index=False, sep=',')
    # write_sub_images(non_greens, output_non_green)


def get_sub_images():
    input = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/roi_samples/orig/banana_plantation_roi1.tif"
    output = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/roi_samples/processed"

    coordinates = list()
    # coordinates.append([[0, 0], [1601, 258]])
    # coordinates.append([[0, 296], [272, 3573]])
    # coordinates.append([[1675, 197], [1926, 3570]])
    # coordinates.append([[3333, 162], [3566, 3577]])
    # coordinates.append([[2612, 1537], [3386, 1838]])
    # coordinates.append([[919, 926], [1721, 1187]])
    # coordinates.append([[972, 3248], [1785, 3577]])
    # coordinates.append([[1820, 219], [2544, 526]])
    # coordinates.append([[1880, 2873], [2612, 3234]])


    coordinates.append([[84, 328], [920, 556]])
    coordinates.append([[796, 1556], [1004, 1928]])
    coordinates.append([[772, 2092], [1052, 2368]])
    coordinates.append([[204, 2604], [904, 2880]])
    coordinates.append([[948, 2564], [1756, 2856]])
    coordinates.append([[1828, 2192], [2692, 2512]])
    coordinates.append([[2464, 1016], [2648, 1268]])
    coordinates.append([[2540, 208], [3328, 496]])
    coordinates.append([[2692, 2512], [3320, 3525]])
    coordinates.append([[1850, 4], [3569, 124]])

    image = Image.open(input)

    for coordinate in coordinates:
        width = coordinate[1][0] - coordinate[0][0]
        height = coordinate[1][1] - coordinate[0][1]
        sub_image = image.crop((
            coordinate[0][0],
            coordinate[0][1],
            width + coordinate[0][0],
            height + coordinate[0][1]))
        sub_image.save(os.path.join(output, "sub_image_x1-{}_y1-{}_x2-{}_y2-{}.tif".format(
            coordinate[0][0],
            coordinate[0][1],
            coordinate[1][0],
            coordinate[1][1]
        )))


def draw_picked_zone():
    coordinates = list()
    coordinates.append([[0, 0], [1601, 258]])
    coordinates.append([[0, 296], [272, 3573]])
    coordinates.append([[1675, 197], [1926, 3570]])
    coordinates.append([[3333, 162], [3566, 3577]])
    coordinates.append([[2612, 1537], [3386, 1838]])
    coordinates.append([[919, 926], [1721, 1187]])
    coordinates.append([[972, 3248], [1785, 3577]])
    coordinates.append([[1820, 219], [2544, 526]])
    coordinates.append([[1880, 2873], [2612, 3234]])
    coordinates.append([[84, 328], [920, 556]])
    coordinates.append([[796, 1556], [1004, 1928]])
    coordinates.append([[772, 2092], [1052, 2368]])
    coordinates.append([[204, 2604], [904, 2880]])
    coordinates.append([[948, 2564], [1756, 2856]])
    coordinates.append([[1828, 2192], [2692, 2512]])
    coordinates.append([[2464, 1016], [2648, 1268]])
    coordinates.append([[2540, 208], [3328, 496]])
    coordinates.append([[2692, 2512], [3320, 3525]])
    coordinates.append([[1850, 4], [3569, 124]])

    input = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/raw/roi/banana_plantation_roi1.jpeg"
    output = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/banana_plantation_roi1_data_zones.jpeg"
    output_test = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/banana_plantation_data_zones_test.jpeg"

    img = mpimg.imread(input)
    plt.imshow(img)

    for i, coordinate in enumerate(coordinates):
        if i >= len(coordinates) - 2:
            # top line
            plt.plot([coordinate[0][0], coordinate[1][0]], [coordinate[0][1], coordinate[0][1]], 'w-', markersize=10)
            # right line
            plt.plot([coordinate[1][0], coordinate[1][0]], [coordinate[0][1], coordinate[1][1]], 'w-', markersize=10)
            # bottom line
            plt.plot([coordinate[1][0], coordinate[0][0]], [coordinate[1][1], coordinate[1][1]], 'w-', markersize=10)
            # left line
            plt.plot([coordinate[0][0], coordinate[0][0]], [coordinate[1][1], coordinate[0][1]], 'w-', markersize=10)
        else:
            # top line
            plt.plot([coordinate[0][0], coordinate[1][0]], [coordinate[0][1], coordinate[0][1]], 'r-', markersize=10)
            # right line
            plt.plot([coordinate[1][0], coordinate[1][0]], [coordinate[0][1], coordinate[1][1]], 'r-', markersize=10)
            # bottom line
            plt.plot([coordinate[1][0], coordinate[0][0]], [coordinate[1][1], coordinate[1][1]], 'r-', markersize=10)
            # left line
            plt.plot([coordinate[0][0], coordinate[0][0]], [coordinate[1][1], coordinate[0][1]], 'r-', markersize=10)

        center = square_center(coordinate[0], coordinate[1])

        plt.text(coordinate[0][0], coordinate[0][1], s="({}, {})".format(coordinate[0][0], coordinate[0][1]),
                 horizontalalignment='left',
                 color='yellow', fontsize=10)

        plt.text(coordinate[1][0], coordinate[1][1], s="({}, {})".format(coordinate[1][0], coordinate[1][1]),
                 horizontalalignment='left',
                 color='#8cff1a', fontsize=10)
    plt.show()
    plt.savefig(output_test)


def square_center(M1, M2):
    C = list()
    C.append((M1[0] + M2[0]) / 2)
    C.append((M1[1] + M2[1]) / 2)

    return C


def extract_no_doublon_from_data():
    input = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/server_db/image_after_review_processed.csv"
    output = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/server_db/image_after_review_no_doublon_processed.csv"

    image_data = pd.read_csv(input, sep=',')
    image_data_without_doublon = image_data[image_data.num_marked == 1]
    image_data_without_doublon.to_csv(output, index=False, sep=',')


def groupe_image_files():
    input_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/server_db/image_after_review_no_doublon_processed.csv"
    output_folder = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/marked_roi_without_doublon/marked_roi_without_doublon"

    if (not os.path.exists(output_folder)):
        os.makedirs(output_folder)

    paths_local = pd.read_csv(input_csv, sep=',').loc[:, ['path_local']].values

    for path_Local in paths_local:
        shutil.copy2(path_Local[0], output_folder)


def augmentation_marked_point():
    input_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/data.csv"
    output_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/data.csv"
    output_folder = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/data"

    if (not os.path.exists(output_folder)):
        os.makedirs(output_folder)

    image_data = pd.read_csv(input_csv, sep=',')

    data_rotate = rotate(list(image_data.values), output_folder)
    data_df = pd.DataFrame(data_rotate)
    data_df.to_csv(output_csv, sep=',', header=list(image_data.columns.values), index=False)


def rotate_marked_point(origin, point, angle):
    """
    Rotate a point counterclockwise by a given angle around a given origin.

    The angle should be given in radians.
    """
    ox, oy = origin
    px, py = point

    qx = round(ox + math.cos(angle) * (px - ox) - math.sin(angle) * (py - oy))
    qy = round(oy + math.sin(angle) * (px - ox) + math.cos(angle) * (py - oy))

    return qx, qy


def rotate(data, output):
    origin = (20, 20)
    res_data = data.copy()

    for row in data:
        shutil.copy2(row[3], output)
        res_data.append(rotate_by_radius(origin, 90, row, output))
        res_data.append(rotate_by_radius(origin, 180, row, output))
        res_data.append(rotate_by_radius(origin, 270, row, output))
        res_data.append(symmetry(row, output, origin, 'LEFT_RIGHT'))
        res_data.append(symmetry(row, output, origin, 'TOP_DOWN'))

    return res_data


def symmetry(row, output, origin, type_flip):
    row_flip = row.copy()
    base_local = row[3].strip(row[1])
    row_flip[1] = 'flip_{}_'.format(type_flip) + row[1]
    row_flip[3] = os.path.join(base_local, row_flip[1])

    image = Image.open(row[3])

    if type_flip == 'LEFT_RIGHT':
        # flip LEFT_RIGHT
        image_flip = image.transpose(Image.FLIP_LEFT_RIGHT)
        if int(row[4]) == 1:
            row_flip[5] = 2 * (origin[0] - row_flip[5]) + row_flip[5]

    else:
        # flip TOP BOTTOM
        image_flip = image.transpose(Image.FLIP_TOP_BOTTOM)
        if int(row[4]) == 1:
            row_flip[6] = 2 * (origin[1] - row_flip[6]) + row_flip[6]

    image_flip.save(os.path.join(output, 'flip_{}_'.format(type_flip) + row[1]))

    return row_flip


def rotate_by_radius(origin, radius, row, output):
    image = Image.open(row[3])

    # rotate 90
    image_rot = image.rotate(-radius)
    image_rot.save(os.path.join(output, 'rotate_{}_'.format(radius) + row[1]))

    row_rot = row.copy()
    base_local = row[3].strip(row[1])
    row_rot[1] = 'rotate_{}_'.format(radius) + row[1]
    row_rot[3] = os.path.join(base_local, row_rot[1])

    if int(row[4]) == 1:
        point = (row_rot[5], row_rot[6])
        row_rot[5], row_rot[6] = rotate_marked_point(origin, point, math.radians(radius))

    return row_rot


def build_image_pixel_dataset():
    input_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/data.csv"
    pixel_data = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/pixel_data.csv"
    target_data = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/target.csv"

    df = pd.read_csv(input_csv, sep=',')
    data_list = list(df.values)

    data = list()
    target = list()

    for i, row in enumerate(data_list):
        percentage(i + 1, len(data_list))
        image = Image.open(os.path.join(cfg.base_path_processed, "all_with_augmented/data/{}".format(row[1])))
        r = np.array(image)[:, :, 0]
        g = np.array(image)[:, :, 1]
        b = np.array(image)[:, :, 2]

        row_img = np.concatenate(
            (np.reshape(r, r.size),
             np.reshape(g, g.size),
             np.reshape(b, b.size))
        )

        row_pixel = list(row_img)

        row_target = list()
        row_target.append(row[4])
        row_target.append(int(row[5]))
        row_target.append(int(row[6]))

        row_pixel = [row[1]] + row_pixel
        row_target = [row[1]] + row_target

        data.append(list(row_pixel))
        target.append(list(row_target))

    data_df = pd.DataFrame(data)
    data_df.to_csv(pixel_data, sep=',', index=False, header=False)

    target_df = pd.DataFrame(target)
    target_df.to_csv(target_data, sep=',', index=False, header=False)


def percentage(n, N):
    if ('linux' in platform.system().lower()):
        os.system('clear')
    if ('windows' in platform.system().lower()):
        os.system('cls')
    os.system('clear')
    print('{}% abgeschlossen'.format(int(n * 100 / N)))


def visualisation_transformation():
    input_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/data.csv"

    fig = plt.figure()
    index = 500
    n = 6630
    m = 5
    f_min_0 = 0 + n
    f_min_i = f_min_0 + m * index
    f_max_i = f_min_i + m
    data = pd.read_csv(input_csv, sep=',').loc[:, ['name', 'center', 'x', 'y']].values
    name = data[index, 0]
    fig.suptitle(name)
    center = data[index, 1] == 1

    x = data[index, 2]
    y = data[index, 3]
    original_name = name

    original = mpimg.imread(
        os.path.join('/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/data',
                     name))
    plt.subplot(231)
    plt.plot(x, y, 'r.', markersize=15)
    plt.title('Original,\n Center: {},\n Center coordinates: ({}, {})'.format(center, x, y), fontsize=10)
    plt.imshow(original, origin="lower")

    i = 2
    for trans_index in range(f_min_i, f_max_i):
        name = data[trans_index, 0]
        center = data[trans_index, 1] == 1
        x = data[trans_index, 2]
        y = data[trans_index, 3]

        transformation = mpimg.imread(os.path.join(
            '/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/data',
            name))
        plt.subplot(int("23{}".format(i)))
        if center:
            plt.plot(x, y, 'r.', markersize=15)
        plt.title(
            'Id: {},\n Center: {},\n Center coordinates: ({}, {})'.format(name.replace(original_name, ''), center, x,
                                                                          y),
            fontsize=10)
        plt.imshow(transformation, origin="lower")
        i += 1

    plt.show()


def split_train_test():
    pixel_data_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/pixel_data.csv"
    target_data_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/target.csv"
    X_train_data_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/X_train.csv"
    Y_train_data_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/Y_train.csv"
    X_test_data_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/X_test.csv"
    Y_test_data_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/Y_test.csv"

    split = 85

    X = pd.read_csv(pixel_data_csv, sep=',').values
    Y = pd.read_csv(target_data_csv, sep=',').values

    data = np.concatenate((X, Y), axis=1)

    np.random.shuffle(data)

    n, m = data.shape

    train = int((n * split) / 100)

    data_train = data[:train, :]
    data_test = data[train:, :]

    X_train = data_train[:, :4801]
    Y_train = data_train[:, 4801:]

    X_test = data_test[:, :4801]
    Y_test = data_test[:, 4801:]

    X_train_df = pd.DataFrame(X_train)
    X_train_df.to_csv(X_train_data_csv, sep=',', index=False, header=False)

    Y_train_df = pd.DataFrame(Y_train)
    Y_train_df.to_csv(Y_train_data_csv, sep=',', index=False, header=False)

    X_test_df = pd.DataFrame(X_test)
    X_test_df.to_csv(X_test_data_csv, sep=',', index=False, header=False)

    Y_test_df = pd.DataFrame(Y_test)
    Y_test_df.to_csv(Y_test_data_csv, sep=',', index=False, header=False)


#
#
# def get_pixel():
#     pixel_data = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/pixel_data.csv"
#     target_data = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/target.csv"
#     X = pd.read_csv(pixel_data, sep=',').values[:, 1:]
#     Y = pd.read_csv(target_data, sep=',').values[:, 1:]
#     r = X[0, :1600].astype(int).reshape((40, 40))
#     g = X[0, :1600].astype(int).reshape((40, 40))
#     r = X[0, :1600].astype(int).reshape((40, 40))
#     print(X.shape, Y.shape)
#     #img = x.reshape((40, 40, 3)).astype(int)
#     plt.imshow(r)
#     plt.show()

def histogram():
    Y_train_data_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/Y_train.csv"
    Y_test_data_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/Y_test.csv"

    y_train = pd.read_csv(Y_train_data_csv, sep=',').values
    y_test = pd.read_csv(Y_test_data_csv, sep=',').values

    y_train_20 = y_train[:, 2] * y_train[:, 3]
    y_test_20 = y_test[:, 2] * y_test[:, 3]
    print(y_train[:, 2])
    print(y_train[:, 3])
    print(y_train[:, 2] * y_train[:, 3])
    y_train_20[y_train_20 == 400] = 1
    y_train_20[y_train_20 != 400] = 0
    print(y_train_20)
    plt.hist(y_train_20 == 40)
    plt.title('Verteilung der Klassen 1 und 0 in der Testmenge')
    plt.show()


def visualisation_used_data():
    data_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/data.csv"
    data_df = pd.read_csv(data_csv)
    data = data_df.values

    input = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/raw/roi/banana_plantation_roi1.jpeg"
    output = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/banana_plantation_roi1_marked_templates.jpeg"
    output_test = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/banana_plantation_field_test.jpeg"

    img = mpimg.imread(input)
    plt.imshow(img)

    for d in data:
        parent_path = d[2]

        M = dict()
        M['x'] = int(d[9])
        M['y'] = int(d[10])

        if ntpath.basename(parent_path) != 'banana_plantation_roi1.jpeg':
            P1, P2 = extract_parent_coordinates_from_path(parent_path)
            _M = coordinates_from_sub_image_to_image(M, P1, P2)
        else:
            _M = M

        # top line
        plt.plot([_M['x'], _M['x'] + 40], [_M['y'], _M['y']], 'r-', markersize=10)
        # right line
        plt.plot([_M['x'] + 40, _M['x'] + 40], [_M['y'], _M['y'] - 40], 'r-', markersize=10)
        # bottom line
        plt.plot([_M['x'] + 40, _M['x']], [_M['y'] - 40, _M['y'] - 40], 'r-', markersize=10)
        # left line
        plt.plot([_M['x'], _M['x']], [_M['y'] - 40, _M['y']], 'r-', markersize=10)

    plt.show()


def coordinates_from_sub_image_to_image(M, P1, P2):
    _M = dict()
    _M['x'] = M['x'] + P1['x']
    _M['y'] = M['y'] + P2['y']

    return _M


def extract_parent_coordinates_from_path(path):
    filename = ntpath.basename(path)
    filename_parts = filename.split('_')

    P1 = dict()
    P2 = dict()

    P1['x'] = int(filename_parts[2].split('-')[1])
    P1['y'] = int(filename_parts[3].split('-')[1])
    P2['x'] = int(filename_parts[4].split('-')[1])
    P2['y'] = int(filename_parts[5].split('-')[1].split('.')[0])

    return P1, P2


def conf_mat():
    train_class_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/models_performances/predictions/prediction_train_class.csv"
    test_class_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/models_performances/predictions/prediction_test_class.csv"

    train = pd.read_csv(train_class_csv).values
    test = pd.read_csv(test_class_csv).values

    train_true = train[:, 0]
    train_predicted = train[:, 1]

    test_true = test[:, 0]
    test_predicted = test[:, 1]

    train_conf_mat = confusion_matrix(train_true, train_predicted, labels=[1, 0])
    test_conf_mat = confusion_matrix(test_true, test_predicted, labels=[1, 0])

    print(train_conf_mat)
    print(test_conf_mat)

    train_precision, train_recall, train_f1_score = get_precision_recall_f1(train_conf_mat)
    test_precision, test_recall, test_f1_score = get_precision_recall_f1(test_conf_mat)

    print('Train: Precision = {}, Recall = {}, F1-Score = {}'.format(train_precision, train_recall, train_f1_score))
    print('Test: Precision = {}, Recall = {}, F1-Score = {}'.format(test_precision, test_recall, test_f1_score))


def get_precision_recall_f1(conf_mat):
    precision = conf_mat[0, 0] / (conf_mat[0, 0] + conf_mat[0, 1])
    recall = conf_mat[0, 0] / (conf_mat[0, 0] + conf_mat[1, 0])
    f1_score = 2 * (precision * recall) / (precision + recall)

    return precision, recall, f1_score


def visualize_predicted_points():
    test_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/test/predictions_{}_stride-{}.csv"
    test_image = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/test/{}.jpeg"

    test_images = [
        'test_sub_image_x1-1850_y1-4_x2-3569_y2-124',
        'test_sub_image_x1-2692_y1-2512_x2-3320_y2-3525']

    test_image_index = 1
    stride_index = 2

    strides = [10, 15, 20]
    zoom_regions = [[[0, 0], [178, 118]], [[354, 47], [469, 154]]]

    img = mpimg.imread(test_image.format(test_images[test_image_index]))
    plt.imshow(img)

    confidences = pd.read_csv(test_csv.format(test_images[test_image_index], strides[stride_index])).values

    coordinates = list()

    for confidence in confidences:
        if confidence[0] >= 1:
            x = confidence[1] + confidence[3]
            y = confidence[2] + confidence[4]

            plt.plot(x, y, 'y.', markersize=10)

            # plt.text(x + 1, y + 1, s="{}".format(confidence[0]),
            #          horizontalalignment='left',
            #          color='yellow', fontsize=10)
            M1 = dict()
            M2 = dict()
            zoom_region = zoom_regions[test_image_index]
            M1['x'] = zoom_region[0][0]
            M1['y'] = zoom_region[0][1]
            M2['x'] = zoom_region[1][0]
            M2['y'] = zoom_region[1][1]
            plot_rectangle(M1, M2, 'b')
            coordinates.append([x, y])

    # X = StandardScaler().fit_transform(np.array(coordinates))
    # db = DBSCAN(eps=0.1, min_samples=2).fit(X)
    # print(db.core_sample_indices_)
    # for indice in range(db.core_sample_indices_.size):
    #     x = coordinates[indice][0]
    #     y = coordinates[indice][1]
    #     plt.plot(x, y, 'r.', markersize=10)

    # clustering(np.array(coordinates), test_images[0], strides[0])
    # plt.title('Vom System herausgefundenene, mögliche Bananenbaumzentren nach Sliding-Window + Zoom-Bereich, stride = {}'.format(strides[stride_index]))
    plt.title('Vom System herausgefundenene, mögliche Bananenbaumzentren nach Sliding-Window, stride = {}'.format(
        strides[stride_index]))
    plt.show()


def mark_point():
    test_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/test/test_sub_image_x1-2692_y1-2512_x2-3320_y2-3525.csv"
    test_output_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/test/test_sub_image_x1-2692_y1-2512_x2-3320_y2-3525_marked.csv"
    test_image = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/test/test_sub_image_x1-2692_y1-2512_x2-3320_y2-3525.jpeg"
    test_image_marked = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/test/test_sub_image_x1-2692_y1-2512_x2-3320_y2-3525_marked.png"

    img = mpimg.imread(test_image)
    plt.imshow(img)

    print(img.shape)
    h = img.shape[0]
    w = img.shape[1]

    coordinates = list()

    points = pd.read_csv(test_csv, sep=',').values

    for i, point in enumerate(points):
        x = point[1]
        y = point[2]
        if x <= w and y <= h:
            plt.plot(x, y, 'y.', markersize=10)
            coordinate = list()
            coordinate.append(i + 1)
            coordinate.append(x)
            coordinate.append(y)
            coordinates.append(coordinate)

    df = pd.DataFrame(coordinates)
    df.to_csv(test_output_csv, sep=',', index=False, header=['num', 'x', 'y'])
    plt.savefig(test_image_marked)
    plt.show()


def plot_rectangle(M1, M2, color):
    # top line
    plt.plot([M1['x'], M2['x']], [M1['y'], M1['y']], '{}-'.format(color), markersize=10)
    # right line
    plt.plot([M2['x'], M2['x']], [M1['y'], M2['y']], '{}-'.format(color), markersize=10)
    # bottom line
    plt.plot([M2['x'], M1['x']], [M2['y'], M2['y']], '{}-'.format(color), markersize=10)
    # left line
    plt.plot([M1['x'], M1['x']], [M2['y'], M1['y']], '{}-'.format(color), markersize=10)


def merge():
    test_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/test/predictions_{}_stride-{}.csv"
    test_image = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/test/{}"
    merged_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/test/merged_prediction_{}_stride-{}.csv"

    test_images = [
        'test_sub_image_x1-1850_y1-4_x2-3569_y2-124',
        'test_sub_image_x1-2692_y1-2512_x2-3320_y2-3525',
        'banana_plantation_roi1'
    ]
    strides = [10, 15, 20]

    test_image_index = 0
    stride_index = 0

    img = mpimg.imread(test_image.format(test_images[test_image_index]) + '.jpeg')
    plt.imshow(img)

    if test_image_index != 2:
        true_values = pd.read_csv(test_csv.format(test_images[test_image_index], strides[test_image_index])).values
        # plt.plot(true_values[:, 1] + true_values[:, 3], true_values[:, 2] + true_values[:, 4], 'y.', markersize=10, label='Manuell markierte Punkte')
        # plt.show()

    begin = datetime.datetime.now()
    total_windows = get_total_template_sliding_windows(img, 40, strides[stride_index])

    predictions = pd.read_csv(test_csv.format(test_images[test_image_index], strides[stride_index])).values
    predictions = predictions[predictions[:, 0] >= 0.5]

    percentage_covered = predictions.shape[0] / total_windows
    print('Estimation of the covered field: {}%'.format(round(100 * percentage_covered, 2)))

    X = predictions[:, 3:]
    template_pos = predictions[:, 1:3]
    X[:, 0] = X[:, 0] + template_pos[:, 0]
    X[:, 1] = X[:, 1] + template_pos[:, 1]

    X_copy = X

    max_param = clustering_kmeans(X_copy, img, strides[stride_index], percentage_covered, scale=False)
    end = datetime.datetime.now()
    print(max_param)
    print('Total time: {}'.format((end - begin).total_seconds()))
    x = max_param['model'].cluster_centers_[:, 0]
    y = max_param['model'].cluster_centers_[:, 1]

    df = pd.DataFrame(np.round(max_param['model'].cluster_centers_[:, :2]))
    df.to_csv(merged_csv.format(test_images[test_image_index], strides[stride_index]), index=False, header=['x', 'y'])
    plt.plot(x, y, 'r.', markersize=10, label='Vom System gefundene Punkte')
    # plt.legend(loc='upper right', shadow=True, fontsize=9, ncol=2, mode="expand")
    plt.show()


def clustering(X, scale=True, max_eps=1.0, eps_step=0.1):
    parameters = list()

    if scale:
        min_max_scaler = MinMaxScaler()
        X = min_max_scaler.fit_transform(X)

    max_silhouette = -100
    max_param = dict()

    for e in np.arange(0.1, max_eps, eps_step):
        for min_sample in range(2, 10):
            try:
                db = DBSCAN(eps=e, min_samples=min_sample, n_jobs=-1).fit(X)

                parameter = list()
                labels = db.labels_
                silhouette = metrics.silhouette_score(X, labels)
                # print('Silhouette: {}, n_core: {}, model: {}'.format(silhouette, db.core_sample_indices_.size, db))
                if silhouette > max_silhouette:
                    max_silhouette = silhouette
                    print(max_silhouette)
                    max_param['silhouette'] = silhouette
                    max_param['model'] = db
                    max_param['n_core_samples'] = db.core_sample_indices_.size
                    if scale:
                        max_param['scaler'] = min_max_scaler
                    else:
                        max_param['scaler'] = None
                parameter.append(e)
                parameter.append(min_sample)
                parameter.append(silhouette)
                parameters.append(parameter)
            except:
                pass
    return max_param


def clustering_kmeans(X, image, stride, covered_percentage, scale=True):
    parameters = list()

    h = image.shape[0]
    w = image.shape[1]

    # n_w = int(((w - 40) / stride) * covered_percentage)
    # n_h = int(((h - 40) / stride) * covered_percentage)
    n_w = int((w - 40) / stride)
    n_h = int((h - 40) / stride)

    # determine the range of center
    max_center = int((n_h * n_w) * covered_percentage / 4)
    min_center = int((n_h * n_w) * covered_percentage / 8)
    print('Range of centers: [{} - {}]'.format(min_center, max_center))

    if scale:
        min_max_scaler = MinMaxScaler()
        X = min_max_scaler.fit_transform(X)

    max_silhouette = -100
    max_param = dict()

    for n_kernel in range(min_center, max_center):
        try:
            kmeans = KMeans(n_clusters=n_kernel, random_state=0, n_jobs=-1, max_iter=500).fit(X)

            parameter = list()
            labels = kmeans.labels_
            silhouette = metrics.silhouette_score(X, labels)
            # print('Silhouette: {}, n_core: {}, model: {}'.format(silhouette, db.core_sample_indices_.size, db))
            if silhouette > max_silhouette:
                max_silhouette = silhouette
                print('Best silhouette: {}'.format(max_silhouette))
                print('Number of center: {}'.format(n_kernel))
                max_param['silhouette'] = silhouette
                max_param['model'] = kmeans
                max_param['n_core_samples'] = kmeans.core_sample_indices_.size
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


def visualize_green_level():
    data_path = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/all_with_augmented/data"

    all_templates = os.walk(data_path)
    means = list()
    for root, folders, templates in all_templates:
        for i, template_name in enumerate(templates[:1000]):
            template = Image.open(os.path.join(data_path, template_name))
            template_matrix = np.array(template)
            _mean_red = np.mean(template_matrix[:, :, 0])
            _mean_green = np.mean(template_matrix[:, :, 1])
            _mean_blue = np.mean(template_matrix[:, :, 2])
            means.append([_mean_red, _mean_green, _mean_blue])
            plt.plot(i, _mean_red, 'rs', markersize=5)
            plt.plot(i, _mean_green, 'gs', markersize=5)
            plt.plot(i, _mean_blue, 'bs', markersize=5)
            # plt.plot([0, len(sub_images)], [80, 80], 'r-', markersize=5)
    plt.title('Mittelwerte der Pixelwerte der roten, grünen und blauen Kanäle pro Template')
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
