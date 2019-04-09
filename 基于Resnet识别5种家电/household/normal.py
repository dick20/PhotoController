from PIL import Image
import os.path
import glob

# 处理图片，切割
def convertjpg(jpgfile,outdir,i,width=28,height=28):
	img=Image.open(jpgfile)
	try:
		new_img=img.resize((width,height),Image.BILINEAR)
		new_img=new_img.convert('RGB')
		new_img.save(os.path.join(outdir,str(i)+'.jpg'))
	except Exception as e:
		print(e)
i = 1
for jpgfile in glob.glob("./fan/*.jpg"):
	convertjpg(jpgfile,"./fan/normalized",i)
	i += 1
i = 1
for jpgfile in glob.glob("./loudspeaker-box/*.jpg"):
	convertjpg(jpgfile,"./loudspeaker-box/normalized",i)
	i += 1
i = 1
for jpgfile in glob.glob("./projector/*.jpg"):
	convertjpg(jpgfile,"./projector/normalized",i)
	i += 1
i = 1
for jpgfile in glob.glob("./television/*.jpg"):
	convertjpg(jpgfile,"./television/normalized",i)
	i += 1