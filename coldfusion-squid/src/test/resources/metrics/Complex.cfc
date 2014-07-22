// Define the ColdFusion componet in script. Notice that the next
// comment block starts with two stars "**". This is crucial if
// you want the following meta data to be applied to the component
// definition. If you only use one star "*" at the beginning of
// the comment block, the "@" properties will be ignored.
//
// Further more, the actual property definition lines must start
// with a single "*" otherwise they will be ignored. I am closing
// the comment block with "**" only for balance - not required.
/**
* @species human
* @kinky:hint Girl object used for blog post on CFScript.
**/
component
    output="false"
    extends="Base"
    hint="I represent a girl object."
    {
 
    // Define object properties. Normally I wouldn't use any
    // property tags; but, by using tags here, ColdFusion 9 will
    // build implicit getters and setters (not actually, but it
    // will handle the getXXX() and setXXX() of the properties.
    // NOTE: It uses the VARIABLES scope to store properties.
    // NOTE: These properties will show up in the CFDump output.
    // NOTE: Implicit getters / setter are about 8x faster than
    // manually created getter/setters.
 
    // Define property using in-line attributes. Notice that
    // when we define things this way, we have to define the
    // name and type using name/value pairs (unlike following
    // examples).
    property
        name="id"
        type="numeric"
        getter="true"
        setter="false"
        default="0"
        hint="The ID of the girl in a persistence context."
        ;
 
    // Define the property using both comments as well as
    // inline attributes.
    //
    // NOTE: This is the only way to get the default value
    // to be an empty string.
    /**
    * @getter true
    * @setter true
    * @validate string
    * @validateParams { minLength=1, maxLength=20 }
    * @hint This is the name of the girl.
    **/
    property
        name="name"
        type="string"
        default=""
        ;
 
    /**
    * @getter true
    * @setter true
    * @validate regex
    * @validateParams { pattern=(?i)(Blonde|Black|Brunette) }
    * @hint This is the hair color of the girl.
    **/
    property hair;
 
    /**
    * @getter true
    * @setter true
    * @validate boolean
    * @default false
    * @hint This is a flag for hotness.
    **/
    property boolean isHot;
 
 
    // Define the component constructor. Remember, ColdFusion 9
    // will look for methods with the name Init() or with the
    // name contained in the initmethod attribute above.
    //
    // Do not use both the "required" keyword AND provide a
    // default value on an argument - it will override the
    // required aspect, making the argument optional.
    //
    // Function attributes can be defined using the following
    // comment block or after the signature (but before the
    // opening brace).
    //
    // NOTE: Using both declarations for a single property will
    // throw a ColdFusion error).
    /**
    * @hint I return an initialized instance.
    * @description I return an initialize Girl object instance.
    **/
    public any function init(
        required string name,
        string hair = "",
        boolean isHot = false
        )
        // Define optional method properties here if you did not
        // define them in the property comment block above.
        output="false"
        {
 
        // Store the properties. Remember, since ID does not have
        // an implicit setter, we have to set it manually.
        variables.id = 0;
 
        // Use implicit stters for the rest of the properites.
        this.setName( arguments.name );
        this.setHair( arguments.hair );
        this.setIsHot( arguments.isHot );
 
        // Return this object reference. Remember, if this method
        // is invoked via the implicit constructor, then it only
        // returns what we return here, so be sure to return THIS.
        return( this );
    }
 
}