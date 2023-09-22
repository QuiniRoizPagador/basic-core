# Basic Core
Proyecto base sobre el que se sustenta el trabajo fin de grado "sistema de recomendación inteligente para toma de decisiones", el cual dio lugar a la aplicación apptc moverse, fruto de la colaboración entre diferentes grados de la universidad pablo de olavide.

estructura básica del proyecto:

![img.png](class_diagram.png)

```
📦main
 ┗ 📂java
 ┃ ┗ 📂es
 ┃ ┃ ┗ 📂roiz
 ┃ ┃ ┃ ┗ 📂basiccore
 ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┣ 📂rest
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AbstractRestController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂websocket
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WebSocketOperations.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜Controller.java
 ┃ ┃ ┃ ┃ ┣ 📂common
 ┃ ┃ ┃ ┃ ┃ ┗ 📂validation
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂impl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AdultValidation.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DniValidation.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PhoneNumberValidation.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Adult.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Dni.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PhoneNumber.java
 ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mapping
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MappingConfig.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂swagger
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SpringFoxConfig.java
 ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Dto.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CrudRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AbstractCrudService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CrudService.java
 ┃ ┃ ┃ ┃ ┣ 📂exeption
 ┃ ┃ ┃ ┃ ┃ ┗ 📜CustomGlobalExceptionHandler.java
 ┃ ┃ ┃ ┃ ┗ 📂infrastructure
 ┃ ┃ ┃ ┃ ┃ ┗ 📜DBEntity.java
```