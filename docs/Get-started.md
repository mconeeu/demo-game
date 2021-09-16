### The IDE
If youre unsure which IDE you should use we recommend [IntelliJ IDEA Community Edition](https://www.jetbrains.com/de-de/idea/).
Its free to download, has all features of Eclipse but has a much more modern interface and is easier and very intuitive to use.
It works very well with with maven and git as well. 

The following explanations are all done using IntelliJ 
but of course you can do all of this in other IDEs or without using an IDE as well (this is of course not recommended).

### Create the project
All projects on MC ONE are made using [Git](https://git-scm.com/) and [Maven](https://maven.apache.org/).
These are the basics in java development, and you should definitely inform yourself about them if you dont know.
If you're using IntelliJ maven is installed automatically. You just have to install Git.

So lets get started. Hit `File > New > Project...`.

![new project window](https://i.imgur.com/vlz4waA.jpg)  
In the New Project window click maven and choose an 1.8 JDK.

![project description window](https://i.imgur.com/hysZGSw.jpg)  
The full name of your plugin should be in lower case and my contain only letters, numbers and hyphens. 
At the front their must be a `mcone-`. Choose a location where you want your source code.  
Definitely pull down the `Artifacts Coordinates` section and fill in the following and click Finish:

**GroupId**: Put `eu.mcone.` at the front, your plugin name in lowercase and without spaces and hyphens behind it.  
**ArtifactId**: Same as Name  
**Version**: As you are just creating the project use `0.0.1-SNAPSHOT`

### Pull in some default files

Copy the following files from this default-game repository to the root of your new project:

| file | description 
| :--- | :--- 
| .gitignore | Prevents IDE specific files from being committed
| .gitlab-ci.yml | Builds your projects jar plugin file automatically if you push
| README.md | Contains information about the project and default coding conventions

> **Important!**  
> Please read and apply the conventions of the *README.md* file for your project and edit this file so that the project name and descriptions are valid!

### Edit the `pom.xml`

Append the following under `<version>0.0.1-SNAPSHOT</version>`:
```xml
<packaging>jar</packaging>

<properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.build.targetEncoding>UTF-8</project.build.targetEncoding>
    <maven.deploy.skip>true</maven.deploy.skip>
</properties>

<developers>
    <developer>
        <!-- Edit this to your personal data -->
        <id>maxmustermann</id>
        <name>Max Mustermann</name>
        <url>https://maxmustermann.de</url>
        <organization>MC ONE</organization>
        <organizationUrl>https://www.mcone.eu</organizationUrl>
        <roles>
            <role>developer</role>
        </roles>
        <timezone>Europe/Berlin</timezone>
    </developer>
    <!-- Add further developers here -->
</developers>

<organization>
    <name>MC ONE</name>
    <url>https://www.mcone.eu</url>
</organization>

<build>
    <finalName>${project.name}-${project.name}-${project.version}</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>2.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-javadoc-plugin</artifactId>
            <version>3.0.1</version>
            <configuration>
                <show>public</show>
                <additionalJOption>-Xdoclint:none</additionalJOption>
            </configuration>
        </plugin>
    </plugins>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <filtering>true</filtering>
        </resource>
    </resources>
</build>

<distributionManagement>
    <repository>
        <id>mcone-systems</id>
        <url>https://repo.onegaming.group/repository/mcone-systems/</url>
    </repository>
</distributionManagement>

<repositories>
    <repository>
        <id>mcone-systems</id>
        <url>https://repo.mcone.eu/repository/mcone-systems/</url>
    </repository>
</repositories>


<dependencies>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.8</version>
        <scope>provided</scope>
    </dependency>
    <dependency> <!-- Spigot (this includes Spigot API, Bukkit API, Craftbukkit and NMS) -->
        <groupId>org.spigotmc</groupId>
        <artifactId>spigot</artifactId>
        <version>1.8.8-R0.1-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
    <dependency> <!-- MCONE BukkitCoresystem -->
        <groupId>eu.mcone.coresystem</groupId>
        <artifactId>bukkit-api</artifactId>
        <version>10.7.0-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
    <dependency> <!-- MCONE GameApi -->
        <groupId>eu.mcone.gameapi</groupId>
        <artifactId>api</artifactId>
        <version>3.4.0-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```
Please edit the `<developers>` section and remove the help comments.
You may add further repositories and dependencies like worldedit, ...

Dont forget to reload your fresh updated maven dependencies using the reload button at the top right.
Alternatively you can update all dependencies using the `Maven` Sidebar on the right in the IntelliJ window.

### Add a plugin.yml

If you didnt know, files that should compile to the root of your jar should be placed in the `src/main/resources` folder.
You can find more information about the plugin.yml syntax [here](https://www.spigotmc.org/wiki/plugin-yml/). 
Knowing that, create a `plugin.yml` in that folder in fill in the following:

| key | value
| :--- | :---
| name | The plugin name (Starts with `MCONE-` and ends with your plugin name in CamelCase, i.e. `MCONE-MyPlugin`)
| authors | The real names of the developers in square brackets (max. 2 names here i.e. `[Rufus Maiwald, Dominik Lippl]`
| version | put here: `${project.version}`. This will automatically insert the maven project version on compile time.
| main | Your main class (i.e.: `eu.mcone.myplugin.MyPlugin`)
| depend | Enter here the names of the plugins you added as dependency (`[MCONE-BukkitCoreSystem, MCONE-GameAPI]`)

### Create the main class and packages

All plugins at mcone have the following root package structure: `eu.mcone.myplugin`
Right click the `src/main/java` folder and click `New > package`.

Please use the [examples from the demo repository](https://gitlab.onegaming.group/minigames/demo/-/blob/master/src/main/java/eu/mcone/demogame/DemoGame.java).
Create the Main Class using in the main package using the plugin name in CamelCase (i.e. `MyPlugin.java`)
Make that Main class extend GamePlugin and add the following code **in** the Main class:

```java
public MyPlugin() {
    super("MyPlugin", ChatColor.RED, "myplugin.prefix");
}

@Override
public void onGameEnable() {
    sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a enabled...");
}

@Override
public void onGameDisable() {
    sendConsoleMessage("§cPlugin disabled!");
}
```

Please edit the `super` call in the constructor to fit to the projects data.

### Create Gitlab project

![Gitlab "Create project" window](https://i.imgur.com/qh426VX.jpg)

[Click here](https://gitlab.onegaming.group/projects/new) to create a new project.
Enter the plugin name (lowercase, starting with `mcone-` i.e. `mcone-myplugin`), choose the project url `mingames` 
and remove the trailing `mcone-` from the project-slug (i.e. `myplugin`)!

Write a short one-sentence description, choose the visibility level `private` and do **not** check `Initialize with README`.
Now `Create Project` and copy the repositories .git url, you need it in the next step.

### Initialize Git Repository

Now its time to do our first initialization commit!
Hit `VCS > Import into Version Control > Create Git Repository` and click `OK`.
After this right click the root folder and choose `Git > Add`.

Now you are ready to commit. Press `Ctrl+K`, enter the commit message `init commit` and click `Commit`.
Push your project to a repository on gitlab using `Ctrl+Shift+K` and enter the remote repositories address from Gitlab (at `Define Remote`).
Click `Push` and you are done!

##### Congratulations! You successfully initialized your plugin repository!