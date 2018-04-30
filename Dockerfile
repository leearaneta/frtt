FROM python:2.7

ENV HOME /home/app

RUN apt-get update && apt-get install -y \
    crf++ \
    ruby

COPY . $HOME/ingredient-phrase-tagger
WORKDIR $HOME/ingredient-phrase-tagger

RUN cd CRF++-0.58 \
 && ./configure --prefix /usr/lib \
 && make \
 && make install \
 && ./configure \
 && make \
 && make install\
 && cd ..

RUN pip install -r requirements.txt && \
    python setup.py install && \
    ./roundtrip.sh

RUN addgroup app && adduser --ingroup app app && \
    chown -R app:app $HOME/*
USER app

CMD python server.py