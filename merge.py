from PIL import Image
import sys
import urllib.request
import requests
import os

def main(back_path, fore_path,x,y) :

    # if netrwork image url with error
    # url = 'https://grim-s3.s3.ap-northeast-2.amazonaws.com/test_page/1.jpg'
    # filepath , _ = urllib.request.urlretrieve(url,back_path)
    # #이미지 파일 오픈
    # r = requests.get(url)
    # with open(filepath,"wb") as outfile:
    #     outfile.write(r.content)

    # iimage url no error
#     urllib.request.urlretrieve(
#   'https://i.pravatar.cc/1000',
#    "1000.png")

    background = Image.open(back_path).convert("RGBA")
    foreground = Image.open(fore_path)

    #배경이 투명한 이미지 파일의 사이즈 가져오기
    
    (img_h, img_w) = foreground.size
    (back_h, back_w) = background.size
    print("charac_size :",foreground.size)
    print("plus x,y : ",x,y)
    #print("img_h : " , img_h , "img_w : ", img_w)

    #합성할 배경 이미지를 위의 파일 사이즈로 resize
    resize_back =  background.resize((img_h, img_w))

    #합성할 배경 이미지에 그대로
    background_back =  background.resize(background.size)

    #x,y = foreground.size
    #이미지 합성
    background_back.paste(foreground, (x,y),foreground)

    #합성한 이미지 파일 보여주기
    background_back.save('/home/g0521sansan/image_processing/story.png')
    # background_back.save('test.png')

if __name__ == "__main__" :
    main(sys.argv[1],sys.argv[2],sys.argv[3],sys.argv[4])
    # 1:original file, 2:저장 위치, 3:시작 x 위치, 4:시작 y 위치
