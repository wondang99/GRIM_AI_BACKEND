# Importing Required Modules
from rembg import remove
from PIL import Image
import sys

def main(input_path) :
    # Store path of the image in the variable input_path
    # input_path =  './sangyoon1.png'
    
    # Store path of the output image in the variable output_path
    # output_path = 'gfgoutput.png'

    character_name = input_path.split('/')[-1].split('.')[0]

    print(input_path)

    output_path = "./remove/"+character_name+'_rm.png'
    print(output_path)
    # Processing the image
    input = Image.open(input_path)
    
    # Removing the background from the given Image
    output = remove(input)

    #Saving the image in the given path
    output.save(output_path)

if __name__ == "__main__" :
    main(sys.argv[1])
