FROM azul/zulu-openjdk:25.0.4.1-jre
WORKDIR /app
ARG PORT
ENV PORT $PORT

# Copy the jar to the production image from the builder stage.
COPY unit-core-plus/target/unit-core-plus-*-spring-boot.jar unit.jar
COPY data/unit_catalog_v2.json /mnt/unit_catalogs/unit_catalog_v2.json

# Add a non-root user
RUN groupadd -g 10001 -r nonroot \
  && useradd -g 10001 -r -u 10001 nonroot
# Run as non-root user
USER 10001:10001

# Run the web service on container startup.
CMD java -Djava.security.egd=file:/dev/./urandom -Dserver.port=${PORT} -Dlog4j.formatMsgNoLookups=true -jar /app/unit.jar
