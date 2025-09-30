History rewrite: removal of large ASC fixtures

What happened
--------------
This repository had two large Advantest `.asc` fixture files tracked in history:

- `5246cef0.asc`
- `SY62669.1_0FN53_06_1_T7N_20250922004144.asc`

These were intentionally removed from the repository history to reduce repository size.

What was done
--------------
- Created a backup branch and tag before rewriting (if you need the original data): these were removed as part of the final purge.
- Rewrote history and removed all references to the two files.
- Expired reflogs, removed leftover `refs/original/*`, and ran aggressive `git gc` locally.

Important: history has been rewritten and `main` was force-updated on the remote.

What you must do (if you have a clone)
------------------------------------
If you have a local clone with the old history, you must reset it to the new remote main. The easiest and safest option is to re-clone. If you must keep your local repo, run:

```bash
# Back up any local branches you need:
git checkout -b my-work-backup
git push origin my-work-backup

# Reset local main to remote main (destructive on local main)
git fetch origingit checkout main
git reset --hard origin/main
```

If you had local branches that were based on the old main, rebase them onto origin/main after the reset.

Questions or recovery
---------------------
If you need the removed ASC files for any reason, contact the repository admin; a temporary backup may exist but was intentionally removed from the published refs.

Revision note
-------------
This file was added after the rewrite to inform collaborators of the change and steps to recover.
