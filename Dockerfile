FROM node:latest

ENV HOME /home/app

RUN npm install --global npm@latest

COPY . $HOME/scraper-gui
WORKDIR $HOME/scraper-gui

RUN useradd --user-group --create-home --shell /bin/false app &&\
    chown -R app:app /home/*

USER app
RUN npm install

CMD npm start