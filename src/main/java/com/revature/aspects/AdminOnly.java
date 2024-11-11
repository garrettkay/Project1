package com.revature.aspects;

//This is a custom annotation!
//We can use it to annotate any controller method that can only be accessed by admins

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //This annotation can only be applied to methods
@Retention(RetentionPolicy.RUNTIME) //The annotation will be available at runtime
public @interface AdminOnly {

    //No need for any fields/methods

    //Our AuthAspect will check for this annotation before allowing HTTP requests

}
