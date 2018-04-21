# mongoDB_JPA_Start02
MongoDB JPA 연동 연습 Act.02[MongoDB, JUnit(repository, service), Mockito MockMVC(controller)]

## Issues
- Repository, Service, Controller 단위 별로 나뉘어서 테스팅을 진행하는 연습을 합니다.
- Mock MVC를 직접 형성하여 Service, Controller 클래스에 대해서도 각각 단위 별 테스팅을 할 수 있도록 보장을 합니다.
- JUnit Config를 추가하여 Spring Application에서 적용하는 Configuration들에 대해서 정상 작동을 보장하도록 합니다.

## Study Docs
스터디 자료는 현재 프로젝트의 `src > doc` 파일에 PDF 파일로 제공을 하였습니다.
 
스터디 자료는 향시에 수정이 될 수 있으니 이 점 참고하시길 바라겠습니다.

[스터디 자료 참고하기](https://github.com/tails5555/mongoDB_JPA_Start02/blob/master/src/doc/MongoDB%2BSpringJPA_02_JUnit_And_Mockito.pdf)

## Maven pom.xml
`pom.xml` 를 기반으로 Maven Dependency를 구성하여 Update Maven은 필수입니다

```
<dependencies>
	<!-- 1. Spring Data JPA Starter -->
	<!-- 이는 실제로 RDBMS에서 하게 된다면 필요하지만, MongoDB Data에서는 MongoRepository를 따로 제공한다. -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-jpa</artifactId>
	</dependency>
	<!-- 2. Spring Data MongoDB Starter -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-mongodb</artifactId>
	</dependency>
	<!-- 3. Spring Web MVC Starter -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
	</dependency>
	<!-- 4. Lombok Project -->
	<!-- Lombok은 각 인스턴스들에 대해서 getter, setter, toString, equals, hashCode 등의 구현을 자동으로 해 주는 프로젝트이다. -->
	<dependency>
		<groupId>org.projectlombok</groupId>
		<artifactId>lombok</artifactId>
		<optional>true</optional>
	</dependency>
	<!-- 5. Tomcat Starter -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-tomcat</artifactId>
		<scope>provided</scope>
	</dependency>
	<!-- 6. Spring Test Starter -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-test</artifactId>
		<scope>test</scope>
	</dependency>
	<!-- 7. MongoDB JDBC Driver -->
	<dependency>
	    <groupId>org.mongodb</groupId>
	    <artifactId>mongo-java-driver</artifactId>
	    <version>3.6.3</version>
	</dependency>
	<!-- 추가적으로 Mockito Mock MVC, JUnit에 관련된 Dependency는 아래와 같이 추가하면 이용할 수 있다. -->
	<dependency>
		<groupId>org.mockito</groupId>
		<artifactId>mockito-all</artifactId>
		<version>1.10.19</version>
	</dependency>
	<dependency>
		<groupId>junit</groupId>
		<artifactId>junit</artifactId>
		<version>4.12</version>
	</dependency>
</dependencies>
```

## Screenshot
![example02_result](/src/doc/example02_result.jpg "example02_result")

## Author
- [강인성](https://github.com/tails5555)