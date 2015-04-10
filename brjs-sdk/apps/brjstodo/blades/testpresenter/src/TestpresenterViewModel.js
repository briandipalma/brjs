'use strict';

var ko = require( 'ko' );
var i18n = require( 'br/I18n' );

function TestpresenterViewModel() {
	this.eventHub = require('service!br.event-hub');
	this.welcomeMessage = ko.observable( 'Welcome to your new Blade.' );
	this.buttonClickMessage = ko.observable( i18n( 'brjstodo.testpresenter.button.click.message' ) );
	this.logWelcome();
}

TestpresenterViewModel.prototype.buttonClicked = function() {
	console.log( 'button clicked' );
	var channel = this.eventHub.channel('testpresenter-channel');
	channel.trigger( 'hello-event', { some: 'Hello World!' } );
};

TestpresenterViewModel.prototype.logWelcome = function() {
	console.log(  this.welcomeMessage() );
}

module.exports = TestpresenterViewModel;
