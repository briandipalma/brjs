'use strict';

var InputViewModel = require( 'brjstodo/input/InputViewModel' );
var ItemsViewModel = require( 'brjstodo/items/ItemsViewModel' );

var KnockoutComponent = require( 'br/knockout/KnockoutComponent' );

var MapUtility = require('br/util/MapUtility');
var EditableProperty = require('br/presenter/property/EditableProperty');
var PresenterComponent = require('br/presenter/component/PresenterComponent');
var PresentationModel = require('br/presenter/PresentationModel');

var br = require( 'br/Core' );

var PresenterThing = require('./PresenterThing');

var r = require('brjstodo/bset/bobblade/BobbladeViewModel');
var App = function() {





	var inputViewModel = new InputViewModel();
	var inputComponent =
		new KnockoutComponent( 'brjstodo.input.view-template', inputViewModel );

	var itemsViewModel = new ItemsViewModel();
	var itemsComponent =
		new KnockoutComponent( 'brjstodo.items.view-template', itemsViewModel );



	//var ep = new EditableProperty("hello");
	var oComponent = new PresenterComponent('brjstodo.input.view-template', new PresenterThing());

	var m_eElement = oComponent.getElement();





  var todoAppEl = document.getElementById( 'todoapp' );

	todoAppEl.appendChild( m_eElement );

	todoAppEl.appendChild( inputComponent.getElement() );
	todoAppEl.appendChild( itemsComponent.getElement() );
};

module.exports = App;
