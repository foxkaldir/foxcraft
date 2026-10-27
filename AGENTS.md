# Project knowledge

- Ground technical decisions in official documentation or primary-source code.
- Follow the `project-docs` skill for repository work. Read `docs/README.md`
  first, then only the sources relevant to the change.
- Keep content declarative and editable: shared catalog and vanilla resource
  files are the source of truth. Keep Fabric and NeoForge adapters thin.
- The supported target is Minecraft 1.21.1 on Fabric and NeoForge. Keep version
  pins in `gradle.properties`; follow `docs/updating-minecraft.md` for ports.
