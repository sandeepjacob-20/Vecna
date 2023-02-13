# Vecna
Vecna is an emotion support app that uses the front facing camera to recognize the user emotion and provide emotional support tips. It recognizes as much as 7 emotion (Happy, Sad, Angry, Surprised, Scared , Disgust, Neutral).
It uses a machine learning model to detect the emotion from an input image and based on the detected emotion, the user is shown a notifcation which takes them to a support page for that emotion.

The model is trained using the FER 2013 dataset from kaggle (https://www.kaggle.com/datasets/msambare/fer2013) combined with the MMA Facial Expression Recognition dataset (https://www.kaggle.com/datasets/mahmoudima/mma-facial-expression/discussion/175213). The FER 2013 dataset contained 48 X 48 pixel grayscale images and the MMA facial expression recognition dataset contained images of a different size which was transformed according to the size of the images in FER 2013 to obtain a combined dataset containing 48,045 training images and 11,924 test images.

The model is a tensorflow lite model which is optimized to be used in android devices. The model takes the face data as input and produces a 2-D array as output. Each row corresponds to the data of each face detected and each row corresponds to the probability value of each of the 7 emotions. The emotion with the highest probability is retured as the result.

This app also uses the google face detection API from the google ML Kit to detect the numer of face in the image. Currently the app is intended to help a single user to deal with day to day emotional trauma and so only a single face can be present in the input.

Furter developments can incorporate an advanced AI chatbot like chatGPT or google Bard to provide more personal and far reaching emotional support to the user. Additionally ML can also be incorporated to find emotional patterns that resembles chronic emotional illnesses like depression, anxiety disorder etc. 

Please feel free to use this repository and add on features and improvements.
