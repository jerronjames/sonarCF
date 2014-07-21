<!--  
1 File 
2 Header
 -->

 <!---   
 3File 
 4Header
 --->

component persistent = "true" hint = "test hint" output = "false" {

// test comment

/**
	 * @moshen:suppress true
	 **/

property name = "testid" ormtype = "int" notnull = "true" length = "10" fieldtype = "id" generator = "identity";

variables.testVar = 13 + 5;
this.testThis = "test";

<!-- 5 sayHello function -->
<!--- 6 sayHello function --->
	function test persistent = "true" hint = "test hint" output = "false" (string action) {
		local.testLocal = action;

		var testV = "test";

		if (testV == action) {
			variables.testVar = 1;
		}

	 	<!--7 comment-->
		  <!--8test-->
		  <!-- 9 say -->
		  <!--- 10 test --->
		  <!--- 11 say --->

		  <!-- 12 test -->
		  <!--- 13 test --->
		  <!-- NOSONAR comment -->
		  <!--- NOSONAR comment --->
	}
	

	<!-- 5 sayHello function -->
<!--- 6 sayHello function --->
	private void function test() {
	 <!--7 comment-->
		  <!--8test-->
		  <!-- 9 say -->
		  <!--- 10 test --->
		  <!--- 11 say --->

		  <!-- 12 test -->
		  <!--- 13 test --->
		  <!-- NOSONAR comment -->
		  <!--- NOSONAR comment --->
	}
}



