from detector.detector import Detector
import pandas as pd

# input image
input_path = '/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/test/test_sub_image_x1-2692_y1-2512_x2-3320_y2-3525.jpeg'

# output folder
output_path = '/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/test/outputs'

# real centers
real_centers_path = '/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/test/test_sub_image_x1-2692_y1-2512_x2-3320_y2-3525_marked.csv'
real = pd.read_csv(real_centers_path, sep=',', header=0).values[:, 1:]

# instantiate the detector
detector = Detector(input_path, output_path)

detector.recognize()

detector.mapping(real)