# ImageProcessor

@**since**: 1.0 > @**api-version**: 2.0 > **Dependencies**: imgscalr, commons-imaging

Files:

* **API Interface** org.europa.together.business.[ImageProcessor](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/business/ImageProcessor.java)
* **Implementation** org.europa.together.application.[ImgSclrProcessor](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/ImgSclrProcessor.java)

---

Application needs often an simple way to manipulate images, like crop or resize. The Image Processor is a Wrapper for the imgsclr and commons-imaging library to enable basic image manipulation.

Available formats for storage: JPEG, JPG, GIF, PNG

**Sample:**

```java
File image = new File("duke_java_mascot.png");
ImageProcessor processor = new ImgSclrProcessor();
processor.loadImage(image);

int inputSize = processor.getImageSize(image);

if(processor.isImageSet()) {
  int height = processor.getHeight();
  int widht  = processor.getWidth();

  // resize the image heigh & weight to the half (reduce)
  BufferedImage picture_01 = processor.resize(50);
  // resize the image heigh & weight to the double (inflate)
  BufferedImage picture_02 = processor.resize(200);

  //rotate in 90 degree clockwise
  BufferedImage picture_03 = processor.rotateRight();
  BufferedImage picture_04 = processor.flipVertical();
  BufferedImage picture_05 = processor.flipHorizontal();

  // cut a defined rectangel (crop) from an image [x,y,height,width]
  BufferedImage picture_05 = processor.crop(1, 1, 50, 80);

  // save as JPEG
  processor.saveImage(picture_01, new File("resize.png"), ImageProcessor.FORMAT_PNG));
}

// clear the internal status of the image processor
processor.clearImage();
```