Web Synchronization
===

Not functional yet, will keep local files in sync with original download locations, useful for javascript source

Usage

- mvn clean install
- java -jar console/target/web-sync.jar config-file.json

To build with dependency analysis

- build [dependency analyzer](https://github.com/SeanShubin/dependency-analyzer)
- mvn clean install -Pdependency
