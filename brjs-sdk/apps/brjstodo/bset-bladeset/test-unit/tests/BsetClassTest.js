(function(){
	'use strict';
	
	var ExampleClassTest = TestCase("ExampleClassTest");
	
	var BsetClass = require("brjstodo/bset/BsetClass");
	
	ExampleClassTest.prototype.testSomething = function()
	{
		assertEquals("hello", new BsetClass().sayHello());
	};
}());