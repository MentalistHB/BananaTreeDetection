import os
from processing.image.processing import *
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import pandas as pd
from PIL import Image
import uuid
from config import Config as cfg


def write_sub_images_without_stride(sub_images, output, input_csv, output_csv, input_parent):
    if (not os.path.exists(output)):
        os.makedirs(output)

    df = pd.read_csv(input_csv, sep=',')
    server_data = list(df.values)

    for sub_image in sub_images:
        row = list()
        name = "{}_x-{}_y-{}_{}_by_{}_widthparent-{}_heightparent-{}.jpg".format(
            sub_image['id'],
            sub_image['x'],
            sub_image['y'],
            sub_image['width'],
            sub_image['height'],
            sub_image['widthparent'],
            sub_image['heightparent']
        )

        sub_image["image"].save(os.path.join(output, name))
        row.append(0)
        row.append(name)
        row.append(input_parent)
        row.append(os.path.join(output, name))
        row.append(1)
        row.append(20)
        row.append(20)
        row.append(sub_image['widthparent'])
        row.append(sub_image['heightparent'])
        row.append(sub_image['x_parent'])
        row.append(sub_image['y_parent'])
        row.append(0)

        server_data.append(row)


    data_df = pd.DataFrame(server_data)
    data_df.to_csv(output_csv, sep=',', header=list(df.columns.values), index=False)


def get_windows_from_coordinate_and_image(image, coordinates, width, height):
    """
    Crop a given image with a stride of stride and return a list of sub images

    :param image:
    :param width:
    :param height:
    :return: sub_images
    """
    # get size of the given image
    image_width, image_height = image.size

    # initialize the list of the resulting sub images
    sub_images = list()

    # go through the horizontal and vertical pixels
    for coordinate in coordinates:
        sub_image_data = dict()

        # compute the new width and height

        new_x_pos = coordinate[0] - (width / 2)
        new_y_pos = coordinate[1] - (height / 2)

        sub_image_width = new_x_pos + width
        sub_image_height = new_y_pos + height

        # verify if the new sub image fit into the general one
        if sub_image_width < image_width and sub_image_height < image_height:
            # crop the image
            sub_image = crop(image, new_x_pos, new_y_pos, sub_image_width, sub_image_height)
            # insert the sub image into the list
            sub_image_data['id'] = str(uuid.uuid4())
            sub_image_data['x'] = coordinate[0]
            sub_image_data['y'] = coordinate[1]
            sub_image_data['width'] = width
            sub_image_data['height'] = height
            sub_image_data['widthparent'] = image_width
            sub_image_data['heightparent'] = image_height
            sub_image_data['x_parent'] = new_x_pos
            sub_image_data['y_parent'] = new_y_pos
            sub_image_data['image'] = sub_image
            sub_images.append(sub_image_data)

    return sub_images


def crop_image_from_coordinates(input, coordinates, output):

    input_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/server_db/image_after_review_final.csv"
    output_csv = "/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/data.csv"

    width = height = 40
    image = Image.open(input)
    sub_images = get_windows_from_coordinate_and_image(image, coordinates, width, height)
    write_sub_images_without_stride(sub_images, output, input_csv, output_csv, input)


def augmentation():
    rotate()
    symmetry()


def rotate():
    all_files = os.walk(cfg.original)

    if (not os.path.exists(cfg.rotate_90_path)):
        os.makedirs(cfg.rotate_90_path)
    if (not os.path.exists(cfg.rotate_180_path)):
        os.makedirs(cfg.rotate_180_path)
    if (not os.path.exists(cfg.rotate_270_path)):
        os.makedirs(cfg.rotate_270_path)

    for root, folders, images in all_files:
        for image_name in images:
            image = Image.open(os.path.join(cfg.original, image_name))

            # rotate 90
            image_90 = image.rotate(90)
            image_90.save(os.path.join(cfg.rotate_90_path, image_name))

            # rotate 180
            image_180 = image.rotate(180)
            image_180.save(os.path.join(cfg.rotate_180_path, image_name))

            # rotate 270
            image_270 = image.rotate(270)
            image_270.save(os.path.join(cfg.rotate_270_path, image_name))


def symmetry():
    all_files = os.walk(cfg.original)

    if (not os.path.exists(cfg.symmetry_horizontal_path)):
        os.makedirs(cfg.symmetry_horizontal_path)
    if (not os.path.exists(cfg.symmetry_vertical_path)):
        os.makedirs(cfg.symmetry_vertical_path)

    for root, folders, images in all_files:
        for image_name in images:
            image = Image.open(os.path.join(cfg.original, image_name))

            # rotate LEFT_RIGHT
            image_90 = image.transpose(Image.FLIP_LEFT_RIGHT)
            image_90.save(os.path.join(cfg.symmetry_vertical_path, image_name))

            # rotate TOP BOTTOM
            image_180 = image.transpose(Image.FLIP_TOP_BOTTOM)
            image_180.save(os.path.join(cfg.symmetry_horizontal_path, image_name))


def visual(image, coordinates):
    img = mpimg.imread(image)
    plt.imshow(img)
    for coordinate in coordinates:
        plt.plot(coordinate[0], coordinate[1], 'rs', markersize=2)
    plt.show()

