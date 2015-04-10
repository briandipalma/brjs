( function(){
	var InputViewModelTest = TestCase( 'InputViewModelTest' );

	var InputViewModel = require( 'brjstodo/input/InputViewModel' );

	InputViewModelTest.prototype.testSomething = function() {
		var model = new InputViewModel();
		assertEquals( '', model.todoText() );
	};
}() );
