# OkJPA
OkJPA is a Java Persistent API framework which is based completely on annotations. No more
XML configurations, no more XML files, just annotations!

# quick start
1. provide the datasource.properties files in the classpath, eg. `project-name/conf/datasource.properties`.
datasource.properties

``` properties
driver = com.mysql.jdbc.Driver
url = jdbc:mysql://127.0.0.1:3306/mybatis3
username = Richard
password = 123456
capacity = 50
```
2. provide the log4j.properties in case of logging, eg. `project-name/conf/log4j.properties`.

```properties
log4j.rootLogger = INFO, MyJPA
log4j.additivity.org.apache = false
log4j.appender.MyJPA = org.apache.log4j.ConsoleAppender
log4j.appender.MyJPA.Threshold = INFO
log4j.appender.MyJPA.ImmediateFlush = true
log4j.appender.MyJPA.layout = org.apache.log4j.PatternLayout
log4j.appender.MyJPA.layout.ConversionPattern = [%p] %m%n%-d{yyyy-MM-dd HH:mm:ss} %c(line%L)%n
```
3. take a pojo class `User` for example
User.java
```java
public class User {
	
	private Long userID;
	private String name;
	private Integer age;
	private String location;
	
	// getter & setter
}
```
4. add data logic in `UserMapper`
UserMapper.java
```java
public interface UserMapper {
	
	@Insert("insert into User(name, age, location) values (${name}, ${age}, ${location})")
	public int addUser(User user);
	
	@Select("select user_id from User")
        public List<Long> getUserIds();
	
	@Delete("delete from User where name = ${ name}")
        public int deleteUsers(@Param("name") String name);
	
	@Select("select count(*) n from User")
        public int getUserCount();
    
}
```
Yes, `UserMapper` is an interface, what you need to do is just telling what you want and
OkJPA will do it for you.

5. Test the logic
```java
public class Test {
    @Test
    public void testGetUserIds() {
        UserMapper userMapper = session.getMapper(UserMapper.class);
        List<Long> list = userMapper.getUserIds(18);
        Assert.assertEquals(2, list.size());
    }
}
```

6. Well done! Enjoy!

# Download
you can download from the [here](https://of7vtvi79.qnssl.com/OkJPA-1.0-SNAPSHOT.jar). 

## author
qinjiangbo1994@163.com
