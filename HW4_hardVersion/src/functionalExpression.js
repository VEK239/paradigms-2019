const OP_LENGTH = {
    "+" : 2,
    "-" : 2,
    "/" : 2,
    "*" : 2,
    "negate" : 1,
    "avg5" : 5,
    "med3" : 3
};
const VARIABLES = {
    'x' : 0,
    'y' : 1,
    'z' : 2
};

const consts = {
    "pi" : Math.PI,
    "e" : Math.E
};

let cnst = (value) => () => value;

function variable(name) {
    let index = VARIABLES[name];
    return (...args) => args[index];
}

anyOp = function (operation) {
    return function () {
        let operands = arguments;
        return function () {
            let res = [];
            let element;
            for (element of operands) {
                res.push(element(...arguments));
            }
            return operation(...res);
        }
    }
};
let med3 = anyOp((a, b, c) => [a, b, c].sort((first, second) => first - second)[1]);
let avg5 = anyOp((a, b, c, d, e) => (a + b + c + d + e) / 5);
let negate = anyOp((a) => -a);
let add = anyOp((a, b) => a + b);
let subtract = anyOp((a, b) => a - b);
let multiply = anyOp((a, b) => a * b);
let divide = anyOp((a, b) => a / b);
let pi = cnst(consts["pi"]);
let e = cnst(consts["e"]);

const OPERRATIONS = {
    "+" : add,
    "-" : subtract,
    "/" : divide,
    "*" : multiply,
    "negate" : negate,
    "avg5" : avg5,
    "med3" : med3
};

operandStack = [];

function parse(st) {
    let expression = st.split(" ").filter((el) => el !== "");
    for (let token of expression) {
        if (token in OPERRATIONS) {
            let operands = [];
            for (let i = 0; i < OP_LENGTH[token]; ++i) {
                operands.push(operandStack.pop());
            }
            operands.reverse();
            operandStack.push(OPERRATIONS[token](...operands));
        } else if (token in VARIABLES) {
            operandStack.push(variable(token));
        } else if (token in consts) {
            operandStack.push(cnst(consts[token]))
        } else {
            operandStack.push(cnst(parseInt(token)));
        }
    }
    return operandStack.pop();
}