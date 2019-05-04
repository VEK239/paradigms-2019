"use strict";

const OP_LENGTH = {
    "+": 2,
    "-": 2,
    "/": 2,
    "*": 2,
    "negate": 1,
    "atan": 1,
    "atan2": 2,
    "min3": 3,
    "max5": 5
};

const CONSTS = {
    "pi": Math.PI,
    "e": Math.E
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
Variable.prototype.toString = function () {
    return this.name;
};
Variable.prototype.diff = function(diffName) {
    return (this.name === diffName) ? ONE : ZERO;
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
Const.prototype.toString = function() {
    return this.value.toString();
};
Const.prototype.evaluate = function() {
    return this.value;
};
Const.prototype.diff = function(diffName) {
    return ZERO;
};
const ONE = new Const(1);
const ZERO = new Const(0);

function Operation(...args) {
    this.operands = args;
}

Operation.prototype.evaluate = function (...args) {
    let result = this.operands.map(op => op.evaluate(...args));
    return this.count(...result);
};
Operation.prototype.toString = function () {
    return this.operands.join(" ") + this.operation;
};
Operation.prototype.diff = function (diffName) {
    let result = this.operands.map(op => op.diff(diffName)).concat(this.operands);
    return this.countDiff(...result);
};

let extendOperation = function (operation, action, diffAction) {
    let resOperation = function () {
        Operation.apply(this, arguments);
        this.operation = operation;
    };
    resOperation.prototype = Object.create(Operation.prototype);
    resOperation.prototype.count = action;
    resOperation.prototype.countDiff = diffAction;
    return resOperation;
};

let Add = extendOperation(' +', (a, b) => a + b, (aDiff, bDiff, a, b) => new Add(aDiff, bDiff));

let Subtract = extendOperation(' -', (a, b) => a - b, (aDiff, bDiff, a, b) =>
    new Subtract(aDiff, bDiff));

let Multiply = extendOperation(' *', (a, b) => a * b, (aDiff, bDiff, a, b) =>
    new Add(new Multiply(a, bDiff), new Multiply(aDiff, b)));

let Divide = extendOperation(' /', (a, b) => a / b, (aDiff, bDiff, a, b) =>
    new Divide(new Subtract(new Multiply(aDiff, b), new Multiply(a, bDiff)), new Multiply(b, b)));

let Negate = extendOperation(' negate', (a) => -a, (aDiff, a) => new Negate(aDiff));

let ArcTan = extendOperation(' atan', (a) => Math.atan(a), (aDiff, a) => new Divide(aDiff,
    new Add(ONE, new Multiply(a, a))));

let ArcTan2 = extendOperation(' atan2', (a, b) => Math.atan2(a, b), (aDiff, bDiff, a, b) =>
    new Divide(new Subtract(new Multiply(b, aDiff), new Multiply(bDiff, a)), new Add(new Multiply(a, a), new Multiply(b, b))));

let Min3 = extendOperation(' min3', (a, b, c) => Math.min(a, b, c), () => ZERO);

let Max5 = extendOperation(' max5', (a, b, c, d, e) => Math.max(a, b, c, d, e), () => ZERO);

const OPERATIONS = {
    "+": Add,
    "-": Subtract,
    "/": Divide,
    "*": Multiply,
    "negate": Negate,
    "atan": ArcTan,
    "atan2": ArcTan2,
    "min3": Min3,
    "max5": Max5
};

function parse(st) {
    let expression = st.split(" ").filter((el) => el !== "");
    let operandStack = [];
    for (let token of expression) {
        if (token in OPERATIONS) {
            let operands = operandStack.splice(operandStack.length - OP_LENGTH[token], OP_LENGTH[token]);
            operandStack.push(new OPERATIONS[token](...operands));
        } else if (token in VARIABLES) {
            operandStack.push(MY_VARS[token]);
        } else if (token in CONSTS) {
            operandStack.push(new Const(CONSTS[token]));
        } else {
            operandStack.push(new Const(parseInt(token)));
        }
    }
    return operandStack.pop();
}