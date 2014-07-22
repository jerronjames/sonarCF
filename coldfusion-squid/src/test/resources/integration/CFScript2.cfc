component
output="false"
hint="I proxy the invocation of a ColdFusion closures and component methods so that they can be more easily invoked from Java."
{


// I get called from Java to invoke a ColdFusion closure. Since
// the Java interface defines this method signature as
// java.lang.Object[], we will only get one argument that is a
// Java Array.
function callClosure( javaArguments ){

    // The first argument being passed-in is the ColdFusion
    // closure to invoke.
    var operator = javaArguments[ 1 ];

    // We need to convert the Java arguments into a ColdFusion
    // argument collection that can be used to invoke the closure.
    var invokeArguments = {};

    // Move the Java arguments in to the closure arguments.
    for (var i = 2 ; i <= arrayLen( javaArguments ) ; i++){

        invokeArguments[ i - 1 ] = javaArguments[ i ];

    }

    // Invoke the closure with the translated argument collection
    // and pass the return value back to the calling context.
    return(
        operator( argumentCollection = invokeArguments )
    );

}


// I get called from Java to invoke a ColdFusion component method.
// Since the Java interface defines this method signature as
// java.lang.Object[], we will only get one argument that is a
// Java Array.
function callMethod( javaArguments ){

    // The first argument is the component instance. The second
    // argument is the method name.
    var target = javaArguments[ 1 ];
    var methodName = javaArguments[ 2 ];

    // We need to convert the Java arguments into a ColdFusion
    // argument collection that be used to invoke the method.
    var invokeArguments = {};

    // Move the Java arguments in to the method arguments.
    for (var i = 3 ; i <= arrayLen( javaArguments ) ; i++){

        invokeArguments[ i - 2 ] = javaArguments[ i ];

    }

    // Invoke the ColdFusion component method with the translated
    // argument collection and pass the return value back to the
    // calling context.
    return(
        invoke( target, methodName, invokeArguments )
    );

}
}