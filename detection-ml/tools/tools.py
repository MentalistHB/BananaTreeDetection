import numpy as np
from PIL import Image

def convertSavedMatrixToApplicableMatrixOfImages(matrix, old_size, new_size):
    m = matrix.shape[0]

    r = matrix[:, :1600]
    g = matrix[:, 1600:3200]
    b = matrix[:, 3200:4800]

    r = r.reshape((m, old_size[0], old_size[1]))
    g = g.reshape((m, old_size[0], old_size[1]))
    b = b.reshape((m, old_size[0], old_size[1]))

    data = np.zeros((m, old_size[0], old_size[1], 3), 'uint8')
    reshaped_data = np.zeros((m, new_size[0], new_size[1], 3), 'uint8')

    data[..., 0] = r
    data[..., 1] = g
    data[..., 2] = b

    for i in range(m):
        img = Image.fromarray(data[i], 'RGB')
        img = img.resize(new_size, Image.BILINEAR)
        reshaped_data[i] = np.asarray(img)

    return reshaped_data