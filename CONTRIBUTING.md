ontributing
## Committing
It is important that the commit message use a specific prefix:

`<type> #<issue number> <commit message>`

Where, `<type>` is:

* "fix": When every you fix anything. Should always contain at least one test.
* "feat": Adding a new feature/function. Should always contain at least one test.
* "doc": Add documentation or editing a markdown file.
* "refactor": Moving/renaming code/files/folder.

### commit-msg git hook
Git hook that enforces a correct commit message.
Copy the following code to <repo>/.git/hooks/commit-msg

```console
#!/usr/bin/env bash

current_branch="$(git rev-parse --abbrev-ref HEAD)"

# regex to validate in commit msg
commit_regex='^((feat|fix|doc|refactor\W#[0-9]+\W\w)|merge)\W.*'
error_msg="Aborting commit. Your commit message is missing either a prefix (feat,fix,doc,refactor) #<issue> or 'Merge'"

if ! grep -iqE "$commit_regex" "$1"; then
    echo "$error_msg" >&2
    exit 1
fi
```
