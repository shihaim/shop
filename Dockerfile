FROM openjdk:latest AS builder

ENV HOME /home/hashi
ENV FILEPATH /hswshop/product_image

RUN mkdir -p $HOME$FILEPATH

COPY hswshop/product_image/test.png $HOME$FILEPATH

COPY ./hsw/shop /tmp
WORKDIR /tmp

RUN ./gradlew build

FROM openjdk:latest
COPY --from=builder /tmp/build/libs/*.jar ./

CMD ["java", "-jar", "shop-0.0.1-SNAPSHOT.jar"]
