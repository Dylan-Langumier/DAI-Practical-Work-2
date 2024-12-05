# Package as docker container

In the main project directory run :

Create imgae
```bash
docker build -t bam .
```

Tag it as latest
```bash
docker tag bam ghcr.io/rp2709/bam:latest
```

Push it to the repo
```bash
docker push ghcr.io/rp2709/bam:latest
```