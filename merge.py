from PIL import Image
import sys
import urllib.request
import requests
import os

def main(back_path, fore_path) :

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
    foreground = Image.open(fore_path).convert("RGBA")

    #배경이 투명한 이미지 파일의 사이즈 가져오기
    (img_h, img_w) = foreground.size
    #print(foreground.size)
    #print("img_h : " , img_h , "img_w : ", img_w)

    #합성할 배경 이미지를 위의 파일 사이즈로 resize
    resize_back =  background.resize((img_h, img_w))

    #합성할 배경 이미지에 그대로
    background_back =  background.resize(background.size)

    x,y = foreground.size
    #이미지 합성
    background_back.paste(foreground, (50,50))

    #합성한 이미지 파일 보여주기
    background_back.save('../story.png')

if __name__ == "__main__" :
    main(sys.argv[1],sys.argv[2])