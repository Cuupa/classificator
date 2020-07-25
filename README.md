# classificator

![CI](https://github.com/Cuupa/classificator/workflows/CI/badge.svg)

## About this project
This project is a simple classification engine.

This project is provided via the MIT-licence, which is free of charge. But if you want to support me, you can spend me a beer or a coffee.
If you want to participate, feel free to create pull requests, fork this project or hit me up with suggestions or code reviews.

THIS IS A WORK IN PROGRESS and done in my spare time.

## How it works
Currently it is just a keyword classification engine with some tweaks. It uses the levensthein algorithm to counter spelling or OCR errors.

It tries to match the topics, sender and metadata, provided in "src/main/resources/kbfiles".
Currently I'm working on lemmatizing and different languages.
