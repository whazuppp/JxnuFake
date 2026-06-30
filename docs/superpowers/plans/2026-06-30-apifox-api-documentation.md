# Apifox API Documentation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Generate an Apifox-importable OpenAPI contract and matching Markdown/PDF API documentation for every currently implemented backend endpoint.

**Architecture:** A single UTF-8 Python generator owns the normalized endpoint metadata and emits all three artifacts so paths, parameters, examples, and schemas cannot drift. The PDF follows the supplied 16-page sample's hierarchy: unified conventions, numbered endpoint sections, request/response tables, JSON examples, and a final endpoint overview.

**Tech Stack:** Python 3, JSON/OpenAPI 3.0.3, ReportLab, pypdf, Poppler.

---

### Task 1: Build the normalized API catalog

**Files:**
- Create: `tools/generate_api_docs.py`
- Read: `JxnuBackEnd/src/main/java/com/sf110/jxnufake/controller/*.java`
- Read: `JxnuBackEnd/src/main/java/com/sf110/jxnufake/dto/*.java`
- Read: `JxnuBackEnd/src/main/java/com/sf110/jxnufake/pojo/*.java`

- [ ] **Step 1: Record all 28 controller mappings**

Create metadata entries containing module, title, HTTP method, backend path, frontend method, authentication requirement, description, request parameters/body, response schema, and examples.

- [ ] **Step 2: Cross-check request fields and validation**

Run:

```powershell
rg -n "@(Get|Post|Put|Delete)Mapping|@RequestParam|@PathVariable|@RequestBody|@Not" JxnuBackEnd/src/main/java/com/sf110/jxnufake
```

Expected: every generated operation and required request field maps to implementation evidence.

### Task 2: Generate the Apifox/OpenAPI artifact

**Files:**
- Create: `docs/Jxnu.apifox.openapi.json`
- Modify: `tools/generate_api_docs.py`

- [ ] **Step 1: Emit OpenAPI 3.0.3 JSON**

Include server `http://localhost:8082`, JWT API-key security header named `token`, module tags, operation IDs, request examples, reusable schemas, and success/error responses.

- [ ] **Step 2: Validate JSON and contract structure**

Run:

```powershell
python -m json.tool docs/Jxnu.apifox.openapi.json
```

Expected: exit code 0.

Run a Python assertion that checks `openapi == "3.0.3"`, 28 operations exist, all path parameters are required, and every protected operation declares `tokenAuth`.

### Task 3: Generate human-readable documentation

**Files:**
- Create: `docs/api-documentation.md`
- Modify: `tools/generate_api_docs.py`

- [ ] **Step 1: Emit Markdown in the sample's structure**

Write unified conventions, response envelope, numbered endpoint sections, parameter tables, request/response examples, implementation notes, and endpoint overview.

- [ ] **Step 2: Check endpoint coverage**

Run an assertion comparing Markdown headings and OpenAPI operation IDs against the normalized catalog.

Expected: 28 headings and 28 unique operation IDs.

### Task 4: Generate and visually verify the PDF

**Files:**
- Create: `output/pdf/Jxnu-前后端接口文档.pdf`
- Create temporarily: `tmp/pdfs/jxnu-api-*.png`
- Modify: `tools/generate_api_docs.py`

- [ ] **Step 1: Emit a polished A4 PDF**

Use Chinese-capable fonts, stable headers/footers, numbered sections, shaded tables, wrapped cells, syntax-styled examples, and automatic page breaks.

- [ ] **Step 2: Verify PDF integrity**

Run `pdfinfo` and parse with `pypdf`.

Expected: nonzero pages, A4 dimensions, extractable title, and all 28 endpoint titles.

- [ ] **Step 3: Render every page**

Run:

```powershell
pdftoppm -png output/pdf/Jxnu-前后端接口文档.pdf tmp/pdfs/jxnu-api
```

Expected: one PNG per PDF page with no rendering errors.

- [ ] **Step 4: Inspect representative and boundary pages**

Review the first page, endpoint transition pages, dense tables, and final overview for clipping, overlap, broken glyphs, and inconsistent spacing. Regenerate until no visual defects remain.

### Task 5: Final verification

**Files:**
- Verify: `docs/Jxnu.apifox.openapi.json`
- Verify: `docs/api-documentation.md`
- Verify: `output/pdf/Jxnu-前后端接口文档.pdf`

- [ ] **Step 1: Re-run the generator**

Expected: deterministic output with no exceptions.

- [ ] **Step 2: Report artifacts and any implementation caveats**

State the endpoint count, validation performed, and clickable absolute paths for all deliverables.
