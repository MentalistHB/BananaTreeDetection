import os

class Config:
    base_path_raw = '/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/raw/'
    base_path_processed = '/media/herval/Save/School/THB/Master/Semestre3/Projekt/data/processed/'
    image_path = base_path_raw + 'roi/banana_plantation_roi1.jpeg'
    coordinates_path = base_path_processed + 'from_herr_boersch/reference_count_roi1.csv'
    croped = base_path_processed + 'from_herr_boersch/croped/'
    original = croped + 'original/'

    augmentation_path = croped + 'augmentation/'

    rotate_path = augmentation_path + 'rotate'
    rotate_90_path = rotate_path + '_90/'
    rotate_180_path = rotate_path + '_180/'
    rotate_270_path = rotate_path + '_270/'

    symmetry_path = augmentation_path + 'symmetry'
    symmetry_vertical_path = symmetry_path + '_vertical'
    symmetry_horizontal_path = symmetry_path + '_horizontal'
    symmetry_first_diagonal_path = symmetry_path + '_first_diagonal'
    symmetry_second_diagonal_path = symmetry_path + '_second_diagonal'
    performanceClustering = os.path.join(base_path_processed, 'test/performance_clustering_{}_{}_stride-{}.csv')