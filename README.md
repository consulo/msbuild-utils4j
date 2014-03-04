msbuild-utils4j
===============

Library for reading and creating MSBuild projects (eg Visual Studio)

## Examples
### Read a project file
```Java
// read the project from file
File projectFile = new File("/projectFolder/projectFile.vcxproj");
ProjectReader projectReader = new ProjectReader();
Project project = projectReader.read(projectFile);

// get a list of all children (Elements)
List<Element> children = project.getChildren();
for(Element element : children){
    // do some stuff...
}
```

### Read some properties
```Java
// get a list of all children (Elements)
List<Element> children = project.getChildren();
for(Element element : children){
    if(element instanceof ItemGroup){
        ItemGroup itemGroup = (ItemGroup)element;
        
        String label = itemGroup.getLabel();
        
        // each implementation provides some basic methods
        List<Item> items = itemGroup.getItems();
    }
}
```

## ToDo
- Implement ProjectWriter for MSBuild project files
- Implement a kind of query to retrieve values
- Provide Wrapper for C++ (vscproj) and C# (csproj)

## License
This project is released under the [Apache License Version 2.0](LICENSE).
