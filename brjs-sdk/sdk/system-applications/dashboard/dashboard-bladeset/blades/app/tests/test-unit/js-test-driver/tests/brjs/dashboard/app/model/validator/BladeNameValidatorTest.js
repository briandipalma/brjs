(function() {
    var ValidationResult = require("br/presenter/validator/ValidationResult");
    var BladeNameValidator = require("brjs/dashboard/app/model/dialog/validator/BladeNameValidator");
    
    BladeNameValidatorTest = TestCase("BladeNameValidatorTest");

    BladeNameValidatorTest.prototype.setUp = function()
    {
        this.m_oValidator = new BladeNameValidator();
    };

    BladeNameValidatorTest.prototype.isValid = function(sBladeName)
    {
        var oValidationResult = new ValidationResult();
        this.m_oValidator.validate(sBladeName, {}, oValidationResult);
        
        return oValidationResult.isValid();
    };

    BladeNameValidatorTest.prototype.testBladeNameCanOnlyBeLowerCaseAlphas = function()
    {
        assertFalse("1a", this.isValid("A"));
        assertFalse("1b", this.isValid("aBc"));
        assertFalse("1c", this.isValid("1a"));
        
        assertTrue("2a", this.isValid("a"));
        assertTrue("2b", this.isValid("abc"));
        assertTrue("2c", this.isValid("a1"));
    };

    BladeNameValidatorTest.prototype.testThatEmptyStringIsAValidBladeName = function()
    {
        assertTrue("1a", this.isValid(""));
    };

    BladeNameValidatorTest.prototype.testStringWithSpacesIsInvalid = function()
    {
        assertFalse("1a", this.isValid("bla bla"));
    };
})();
