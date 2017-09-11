# CryptoVison

MD5 Data Encryptor.

_The CryptoVision project was originally developed as part of a Computer Science BSc Degree._

## About

A segment of a larger piece of security suite software (CryptoVision) to allow data (images/text) to be encrypted/decrypted using an MD5 crypto algorithm.

## Getting Started

The encryptor itself is a single Jar file however some test images/text are included. To run the encryptor, navigate to the folder in which the Jar is contained, and run the follow from a terminal/command line.

```java -jar CryptoVision.jar
```

You can specify the data file to process straight from the command line.

```java -jar CryptoVision.jar Path/To/File
```

You can also specify the encryption key from the command line.

```java -jar CryptoVision.jar Path/To/Image/File KeyToUse
```

## Repo

- `/src/`	
	CryptoVision source
- `/test/`	
	Sample test data
- `README.md`	
	This readme file
- `CryptoVision.jar`	
	The runnable data encryptor