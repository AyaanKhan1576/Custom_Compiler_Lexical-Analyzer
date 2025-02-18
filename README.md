Compiler Construction
Ayaan Khan  (22i-0832 | CS-K)
Minahil Ali (22i-0849 | CS-G)

---

# 📜 Ye Olde Code (.yoc) Language Specification

## 📖 Overview  
**Ye Olde Code** is a programming language inspired by C-like syntax and structure, infused with Ancient English terminology. The language uses the `.yoc` file extension.  

---

## 🔍 Lexical Analyzer Overview  
The lexical analyzer for Ye Olde Code works in several stages:  

### 1. 🔠 Regular Expressions (RE)  
- Every token type (e.g., identifiers, keywords, literals, operators) is defined using a regular expression to capture its structure.

### 2. 🔄 Conversion to NFA  
- Infix regular expressions are converted to postfix notation to simplify NFA construction.  
- **Thompson’s Construction** is used to convert postfix regex into a **Non-Deterministic Finite Automaton (NFA)**.

### 3. 🔁 Conversion to DFA  
- The combined NFA is transformed into a **Deterministic Finite Automaton (DFA)** using the subset construction algorithm.  
- The DFA efficiently scans the input text by eliminating NFA non-determinism.

### 4. 🔖 Token Generation  
- The DFA scans the input file to produce tokens (lexemes paired with their token types).  
- Uppercase characters (outside of string literals) are automatically converted to lowercase, maintaining case insensitivity with warnings to the user.

### 5. ⚠️ Error Reporting  
- Lexical errors (e.g., unrecognized symbols) are reported with messages in archaic language, preserving the theme of Ye Olde Code.

---

## 🔢 Data Types  
- **`number`** (formerly `int`): Integer values  
- **`letter`** (formerly `char`): Single character values  
- **`fraction`** (formerly `float`): Single-precision floating-point  
- **`twainfraction`** (formerly `double`): Double-precision floating-point  
- **`text`** (formerly `string`): Text strings  
- **`truth`** (formerly `boolean`): Boolean values  
- **`nothing`** (formerly `void`): Void type  

---

## 🔑 Keywords and Control Flow  

### 📌 Declaration and Modifiers  
- **`forever`** → `const`: Constant values  
- **`unmoving`** → `static`: Static variables  
- **`callith`** → `typedef`: Type definition  
- **`binding`** → `struct`: Structure definition  
- **`chooseth`** → `enum`: Enumeration  

### 🔄 Control Structures  
- **`perchance`** → `if`: Conditional statement  
- **`otheryonder`** → `else`: Alternative condition  
- **`whilst`** → `while`: While loop  
- **`fortime`** → `for`: For loop  
- **`maketh`** → `do`: Do-while loop  
- **`shatter`** → `break`: Break statement  
- **`forthwith`** → `continue`: Continue statement  

### 🔧 Functions and Returns  
- **`giveth`** → `return`: Return statement  
- **`howbig`** → `sizeof`: Size operator  

### 📥 Input/Output  
- **`sayeth`** → `printf`: Print to console  
- **`heareth`** → `scanf`: Read from console  

---

## ➕ Operators  
- **Arithmetic:** `+`, `-`, `*`, `/`, `%`, `^`  
- **Comparison:** `<`, `>`, `<=`, `>=`, `==`, `!=`  
- **Assignment:** `=`  
- **Logical:** `&&`, `||`, `!`  

---

## 💬 Comments  
- **Single-line:** `// Verily, this be a comment`  
- **Multi-line:** `/* Lo and behold, multiple lines of wisdom */`  

---

## ❗ Error Handling  
- **Syntax Error:** `"Forsooth! Thine syntax is amiss"`  
- **Type Error:** `"Alas! The types do not match"`  
- **Undefined Error:** `"Hark! This symbol is unknown"`  
- **Scope Error:** `"Thou cannot access this variable from this realm"`  

---

## 💻 Example Code
```c
// Declaration
number score = 10;
forever number MAX_POINTS = 100;
truth isValid = true;

// Function
nothing calculateScore(number points) {
    perchance (points > MAX_POINTS) {
        sayeth("Thou hast exceeded the maximum!");
        giveth;
    }
    otheryonder {
        sayeth("Thy score is valid!");
    }
}

// Loop
fortime (number i = 0; i < 10; i = i + 1) {
    sayeth("Counting: %d", i);
}
```

---
