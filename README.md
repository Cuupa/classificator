# classificator

![GitHub](https://img.shields.io/github/license/Cuupa/classificator) ![CI](https://github.com/Cuupa/classificator/workflows/CI/badge.svg) ![GitHub issues](https://img.shields.io/github/issues-raw/Cuupa/classificator) ![GitHub pull request](https://img.shields.io/github/issues-pr-raw/Cuupa/classificator)

## Content
- [About this project](https://github.com/Cuupa/classificator#about-this-project)
- [How to contribute](https://github.com/Cuupa/classificator#how-to-contribute)
- [How it works](https://github.com/Cuupa/classificator#how-it-works)

## About this project
This project is a simple classification engine.

This project is provided via the MIT-licence, which is free of charge. But if you want to support me, you can spend me a beer or a coffee.
If you want to participate, feel free to create pull requests, fork this project or hit me up with suggestions or code reviews.

THIS IS A WORK IN PROGRESS and done in my spare time.

## How to contribute
If you want to participate, feel free to create pull requests, fork this project, create new issues or hit me up with suggestions.
When creating an issue or a pull request, please be as detailed as possible.

"I want to participate, but I know nothing about programming 😔"
- No problem. You can contribute by providing [topic definitions](https://github.com/Cuupa/classificator/tree/master/src/main/resources/kbfiles) or contribute by providing feedback, make some suggestions eg.
If you want to create or fine tune a topic, create a pull request and I'll give it a shot.

If you think this project is awesome, you can spend me a beer or a coffee.

![BuyMeACoffee](https://img.shields.io/badge/Support%20%20me-Buy%20me%20a%20coffee-success?logo=buymeacoffee&link=https://buymeacoff.ee/Cuupa)

[Direct link](https://buymeacoff.ee/Cuupa)

## How it works
Currently it is just a keyword classification engine with some tweaks. It uses the levensthein algorithm to counter spelling or OCR errors.

It tries to match the topics, sender and metadata, provided in "src/main/resources/kbfiles".

If no sender matches, it tries to determine the sender via REGEX, removing the ones with more then six words, counting the occurences in the text and taking the occurences times the length of the String.

Currently I'm working on lemmatizing and different languages.
