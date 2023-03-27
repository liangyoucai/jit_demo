# Git 0.0

A simple version control system. 

## Before use

- **Environment setting**:  CLASSPATH : ...\jit_demo\out\production\jit_demo

- **Encoding**: UTF-8

- **cmd command**: 

  - java main.git <command> <option>
  - **NOTE**: when using cmd window, please input commands in **the destination folder**. 

  For example,"...\jit_demo\src\singers" for Client and "...\jit_demo\src\git_SERVER" for Server.

  To see more information about Git, use `java main.git` or `java main.git help`.

## Main functions

- Implements the git commands `init`, `add`, `commit`, `rm`, `log`, `reset`, `remote`, `push` and `pull`. 
- **Note**: Run the **Server** before using **push/pull** each time. The default “origin” has been set to the local host. To start the Server, use `java main.Server run`. To see more information about Server, use `java main.Server` or `java main.Server help`.
