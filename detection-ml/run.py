from detector.detector import Detector

input_path = '/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/test/test11.jpeg'
# output folder
output_path = '/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/test/outputs'

# instantiate the detector
detector = Detector(input_path, output_path)

detector.recognize()

