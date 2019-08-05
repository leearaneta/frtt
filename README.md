# frtt
From Recipe to Table

# Intro

FRTT scrapes ingredients from a recipe and transfers those ingredients to a list.
If multiple recipe URLs are provided, FRTT will perform calculations to combine ingredients and units.
This project uses [nytimes/ingredient-phrase-tagger](https://github.com/nytimes/ingredient-phrase-tagger) to parse phrases of ingredients and [Selenium Hub](https://hub.docker.com/r/selenium/hub/) to scrape.

# Build Instructions

`docker compose build`
`docker compose up`
