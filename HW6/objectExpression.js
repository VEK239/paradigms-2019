"use strict";
const OP_LENGTH = {
    "+": [2, 1],
    "-": [2, 1],
    "/": [2, 1],
    "*": [2, 1],
    "negate": [1, 6],
    "sum": [Infinity, 3],
    "avg": [Infinity, 3]
};
const VARIABLES = {
    "x": 0,
    "y": 1,
    "z": 2
};

function Variable(name) {
    this.name = name;
    this.index = VARIABLES[this.name];
}

Variable.prototype.evaluate = function (...args) {
    return args[this.index];
};
Variable.prototype.prefix = function () {
    return this.name;
};
const VAR_X = new Variable('x');
const VAR_Y = new Variable('y');
const VAR_Z = new Variable('z');

const MY_VARS = {
    "x": VAR_X,
    "y": VAR_Y,
    "z": VAR_Z
};
let Const = function (x) {
    this.value = x;
};
Const.prototype.prefix = function () {
    return this.value.toString();
};
Const.prototype.evaluate = function () {
    return this.value;
};

function Operation(...args) {
    this.operands = args;
}

Operation.prototype.evaluate = function (...args) {
    let result = this.operands.map(op => op.evaluate(...args));
    return this.count(...result);
};
Operation.prototype.prefix = function () {
    return '(' + this.operation + " " + this.operands.map(op => op.prefix()).join(" ") + ')';
};
let extendOperation = function (operation, action) {
    let resOperation = function () {
        Operation.apply(this, arguments);
        this.operation = operation;
    };
    resOperation.prototype = Object.create(Operation.prototype);
    resOperation.prototype.count = action;
    return resOperation;
};

let Add = extendOperation('+', (a, b) => a + b);
let Subtract = extendOperation('-', (a, b) => a - b);
let Multiply = extendOperation('*', (a, b) => a * b);
let Divide = extendOperation('/', (a, b) => a / b);
let Negate = extendOperation('negate', (a) => -a);
let Sum = extendOperation('sum', function (...args) {
    if (args.length === 0) {
        return 0;
    } else {
        return args.reduce((x, y) => x + y);
    }
});
let Avg = extendOperation('avg', function (...args) {
    if (args.length === 0) {
        return NaN;
    } else {
        return args.reduce((x, y) => x + y) / args.length;
    }
});

const OPERATIONS = {
    "+": Add,
    "-": Subtract,
    "/": Divide,
    "*": Multiply,
    "negate": Negate,
    "sum": Sum,
    "avg": Avg
};

function ParsingError(message) {
    this.message = message;
}
ParsingError.prototype = Object.create(Error.prototype);
ParsingError.prototype.name = "ParsingError";
ParsingError.prototype.constructor = ParsingError;

let index = 0;
let expression;


function skipWhiteSpace() {
    while (index < expression.length && /\s/.test(expression[index])) {
        ++index;
    }
}

function getPrefixOperation(expectingOperand) {
    skipWhiteSpace();
    if (index >= expression.length) {
        throw new ParsingError('Not expected end of expression at position: ' + index);
    }
    if (expression[index] === '(') {
        ++index;
        let result = getPrefixOperation(false);
        skipWhiteSpace();
        if (expression[index] !== ')') {
            throw new ParsingError('Expected closing brace at position: ' + index);
        }
        index++;
        return result;
    }
    let start = index;
    while (!/\s/.test(expression[index]) && !'()'.includes(expression[index]) && index < expression.length) {
        ++index;
    }
    let token = expression.substr(start, index - start);
    //console.log(token);
    if (!expectingOperand) {
        if (token in OPERATIONS) {
            let operandStack = [];
            skipWhiteSpace();
            if (OP_LENGTH[token][0] === Infinity) {
                while (expression[index] !== ')') {
                    let result = getPrefixOperation(true);
                    skipWhiteSpace();
                    operandStack.push(result);
                }
            } else {
                for (let i = 0; i < OP_LENGTH[token][0]; ++i) {
                    operandStack.push(getPrefixOperation(true));
                }
            }
            return new OPERATIONS[token](...operandStack);
        }
        throw new ParsingError('Expected operation, found "' + token + '" at position: ' + index);
    } else {
        if (token in VARIABLES) {
            return MY_VARS[token];
        } else if (token.length > 0 && '-0123456789'.includes(token[0])) {
            if (!isNaN(token)) {
                return new Const(parseFloat(token));
            } else {
                throw new ParsingError('Expected number, found "' + token + '" at position: ' + (index - token.length))
            }
        }
        throw new ParsingError('Expected operand, found "' + token + '" at position: ' + index);
    }
}

function parsePrefix(st) {
    //console.log(st)
    expression = st.trim();
    index = 0;
    if (expression.length === 0) {
        throw new ParsingError('Empty expression');
    }
    let result = getPrefixOperation(true);
    if (index < expression.length) {
        throw new ParsingError('Expected end of expression at position: ' + index);
    }
    return result;
}