/**
 * Centralised Playwright selectors for the WebProtege GWT UI.
 *
 * Selector precedence:
 *   1. `#gwt-debug-...` — emitted by GWT (`debugId` UiBinder attribute calls
 *      `ensureDebugId`, which sets `id="gwt-debug-<name>"` once
 *      `com.google.gwt.user.Debug` is inherited; see
 *      webprotege-gwt-ui-client/src/main/module.gwt.xml). Stable across i18n
 *      changes and rebuilds.
 *   2. `wp-*` CSS classes from .ui.xml templates (e.g. wp-login__form).
 *      Stable as long as the .ui.xml class name is not renamed.
 *   3. Visible text from Messages.java — least stable but useful as a
 *      fallback for buttons that lack debugId or distinguishing class.
 *
 * Whenever a test needs a stable handle that does not yet exist in the DOM,
 * add a `debugId` attribute to the corresponding .ui.xml template in the
 * same commit as the test, and register the selector here.
 */

export const Login = {
  form: '.wp-login__form',
  username: '#username, .wp-login__form .gwt-TextBox',
  password: '#password, .wp-login__form .gwt-PasswordTextBox',
  submit: '#kc-login',
  /** webprotege-keycloak login.ftl renders form errors in
   * `<span class="wp-input-error">` and Keycloak's default theme uses
   * `#input-error` / `.kc-feedback-text` — accept either. */
  error: '.wp-input-error, #input-error, .kc-feedback-text, .alert-error',
} as const;

export const TopBar = {
  root: '#gwt-debug-TopBarContainer',
  projectTitle: '.wp-topbar__title',
  homeButton: '.wp-topbar__btn:has-text("Home")',
  signOut: 'text=Sign Out',
  /** Logged-in user button on the top bar shows "<email> ▾" (rendered via
   * LoggedInUserPresenter). It's a plain `wp-topbar__btn` with the user
   * email as its visible text. */
  userMenu: 'button.wp-topbar__btn:has-text("▾"):has-text("@")',
  helpLink: '.wp-topbar__btn:has-text("Help")',
} as const;

export const ProjectList = {
  root: '.wp-project-list',
  rows: '.wp-project-list__rows__row',
  nameCell: '.wp-project-list__name-col',
  ownerCell: '.wp-project-list__owner-col',
  lastOpenedCell: '.wp-project-list__last-opened-col',
  lastModifiedCell: '.wp-project-list__last-modified-col',
  /** The row's overflow menu is a `popupmenu:MenuButton` widget — an
   * `HTMLPanel` (a `<div>`) with `title="Menu"`, NOT a `<button>` element. */
  menuButton: '.wp-project-list__menu-button-col[title="Menu"]',
  /** GWT `g:CheckBox` renders `<span class="gwt-CheckBox"><input ...><label
   * for="...">X</label></span>` — i.e. the input is a SIBLING of the label,
   * not its parent. Match the `.gwt-CheckBox` wrapper that contains the
   * label and target the contained `input`. */
  filters: {
    ownedByMe: '.gwt-CheckBox:has(label:has-text("Owned by Me")) input',
    sharedWithMe: '.gwt-CheckBox:has(label:has-text("Shared with Me")) input',
    trash: '.gwt-CheckBox:has(label:has-text("Trash")) input',
  },
  sortDropdown: 'select',
  createButton: 'button:has-text("Create New Project")',
} as const;

export const CreateProjectDialog = {
  root: '.wp-modal',
  name: '.wp-modal input[type="text"]',
  description: '.wp-modal textarea',
  language: '.wp-modal .formField input',
  fileUpload: '.wp-modal input[type="file"]',
  submit: '.wp-modal button.wp-btn--dialog.wp-btn--primary',
  cancel: '.wp-modal button.wp-btn--dialog:has-text("Cancel")',
  nameError: '.wp-modal >> text=Please enter a project name',
} as const;

export const ProjectView = {
  root: '#gwt-debug-ProjectView',
  perspectiveSwitcher: '#gwt-debug-PerspectiveSwitcher',
  /** Match a perspective tab by its visible label. Top-level perspectives
   * live in the `#gwt-debug-PerspectiveSwitcher` TabBar; sub-tabs for
   * Object/Data/Annotation Properties live inside the PropertyHierarchy
   * portlet, NOT under the PerspectiveSwitcher — so this selector matches
   * any GWT TabBarItem whose label is exact. The portlet sub-tabs only
   * render once the parent "Properties" perspective is opened. */
  tab: (label: string) =>
    `.gwt-TabBarItem:has(.gwt-Label:text-is("${label}"))`,
  addTabButton: 'button:has-text("Tabs")',
  /** Property sub-tabs ("Object Properties", "Data Properties",
   * "Annotation Properties") live inside PropertyHierarchyPortletView's
   * inner TabBar, which only attaches once "Properties" is opened. */
  propertiesSubTabs: ['Object Properties', 'Data Properties', 'Annotation Properties'],
} as const;

export const Hierarchy = {
  /** Generic tree-node locator by visible label. The graphtree library
   * (edu.stanford.protege.gwt.graphtree) renders rows under `.gt-tree`.
   * Returns the row itself (not the inner text node) so clicks aren't
   * intercepted by the draggable row container. */
  treeNode: (label: string) => `.gt-tree__row:has(:text-is("${label}"))`,
  selectedNode: '.gt-tree__row--selected',
  toolbar: {
    /** Hierarchy "Create" / "Delete" buttons are icon-only and rely on
     * a hovering tooltip rather than a `title` attribute. Match the GWT
     * style class added in `*HierarchyPortletPresenter` instead.
     * Note: there is NO watch toolbar button — `wp-btn-g--watch` is not
     * emitted by any presenter; watching goes through the tree context
     * menu ("Watch..." item, see the `Watches` group below). */
    create: 'button.wp-btn-g--create',
    delete: 'button.wp-btn-g--delete',
  },
  /** PopupMenu (`edu.stanford.bmir.protege.web.client.library.popupmenu`)
   * renders as `<div class="wp-popup-menu">` with `<div class="wp-popup-menu__item">`
   * children — there is no `role="menu"`/`role="menuitem"` markup. */
  contextMenu: '.wp-popup-menu',
  contextMenuItem: (label: string) => `.wp-popup-menu__item:has-text("${label}")`,
} as const;

export const FrameEditor = {
  root: '.frame-editor, .wp-class-frame-editor',
  iri: '.iri-field',
  annotationsSection: 'text=Annotations',
  classesSection: 'text=Classes',
  relationshipsSection: 'text=Relationships',
  /** A labelled section inside a frame editor — `.wp-form-group` wraps each
   * row, with a `.wp-form-label` heading set from `Messages.java` (e.g.
   * "Annotations", "Domain", "Range", "Relationships", "Types", "Parents").
   * Match exact text so "Annotations" doesn't also catch "Annotation
   * Properties". */
  section: (label: string) =>
    `.wp-form-group:has(.wp-form-label:text-is("${label}"))`,
  /** A row inside a `ValueListFlexEditorImpl`-backed list editor
   * (PrimitiveDataListEditor or PropertyValueListEditor). */
  row: '.wp-value-list__row',
} as const;

export const CreateEntityDialog = {
  root: '.wp-modal',
  /** "Class names" / "Property names" / etc — a textarea (one name per line). */
  name: '.wp-modal textarea',
  /** Language tag input — second text field in the dialog. */
  langTag: '.wp-modal input[type="text"]',
  submit: '.wp-modal button.wp-btn--dialog.wp-btn--primary',
  cancel: '.wp-modal button.wp-btn--dialog:has-text("Cancel")',
} as const;

export const Search = {
  /** The hierarchy/individuals portlets register a search button via
   * `wp-btn-g--search` (PortletAction in `*HierarchyPortletPresenter`). */
  trigger: 'button.wp-btn-g--search',
  /** The search dialog opens as a `wp-modal`. The header label is "Search". */
  modal: '.wp-modal:has-text("Search")',
  /** Search field is a PlaceholderTextBox (an `<input type="text">`). */
  input: '.wp-modal input[type="text"]',
  /** Search results render an EntitySearchResultViewImpl per match — the
   * stable hook is the entity display-name span (`wp-entity-node__display-name`).
   * The result list mounts inside the search modal, but be lenient and match
   * any display-name on the page (the only other source is the hierarchy
   * tree underneath, which is hidden behind the modal overlay). */
  result: '.wp-entity-node__display-name',
} as const;

export const Sharing = {
  /** The topbar "Share" button on a project routes to a SharingSettingsPlace
   * — a full page, NOT a dialog. The page has a link-sharing checkbox, a
   * permission dropdown, and a value-list editor for "Share with people". */
  pageRoot: '.wp-form-group:has(input[type="checkbox"])',
  linkSharingToggle: '.gwt-CheckBox input[type="checkbox"]',
  linkSharingPermission: 'select',
  /** The "Share with" list is a ValueListFlexEditorImpl — same
   * `wp-value-list` bundle classes the frame editor uses (the editor runs
   * in AUTOMATIC new-row mode, so a trailing blank row is always present). */
  shareWithList: '.wp-value-list',
  personRow: '.wp-value-list__row',
  /** Person field inside a row is a GWT SuggestBox backed by
   * UserIdSuggestOracle. `SharingSettingEditorImpl.getValue()` reads the
   * raw text, so typing a full username + blur is enough — no suggestion
   * selection needed. Use pressSequentially, never fill(). */
  personInput: 'input.gwt-SuggestBox',
  /** Permission ListBox — option VALUES are the enum names
   * VIEW | COMMENT | EDIT | MANAGE (labels are lowercased only by CSS),
   * stable across i18n. Scope under a row locator. */
  permissionSelect: 'select',
  /** Settings-page Apply button (SettingsViewImpl.ui.xml). */
  applyButton: 'button.wp-btn--accept:has-text("Apply")',
  suggestPopup: '.gwt-SuggestBoxPopup',
  topBarButton: 'button.wp-topbar__btn:has-text("Share")',
} as const;

export const Comments = {
  /** Comments portlet toolbar action (EntityDiscussionThreadPortletPresenter).
   * Icon-only; enabled only while an entity is selected. The portlet sits
   * in the right-hand column of the default Classes perspective. */
  startThreadButton: 'button.wp-btn-g--create-thread',
  /** CommentEditorViewImpl hosts a CodeMirror-5 editor inside a wp-modal.
   * fill() cannot reach it — click the .CodeMirror element, then
   * page.keyboard.type(). Avoid '@' in bodies (mention autocomplete). */
  editor: '.wp-modal .CodeMirror',
  editorOk: '.wp-modal button.wp-btn--dialog.wp-btn--primary',
  /** discussion.css declares these @external, so the names survive the
   * GWT CssResource obfuscation pass. */
  thread: '.wp-disc-thread__outer',
  /** Text is "Resolve" while the thread is open, "Re-open" once closed. */
  threadStatus: 'button.wp-disc-thread__status',
  comment: '.wp-comment',
  commentBody: '.wp-comment__body',
  replyButton: '.wp-disc-thread__outer button:has-text("Reply")',
} as const;

export const Watches = {
  /** WatchPresenter modal (title "Watches"). Opened from the hierarchy
   * context-menu item "Watch...". Reopening the dialog re-reads state via
   * GetWatchesAction, so it doubles as a persistence assertion. */
  modal: '.wp-modal:has-text("Watches")',
  /** WatchViewImpl.ui.xml radio labels are hardcoded (not i18n). GWT
   * RadioButton = <span class=gwt-RadioButton><input><label>. */
  radio: (label: 'None' | 'Entity' | 'Branch') =>
    `.wp-modal .gwt-RadioButton:has(label:text-is("${label}")) input`,
  ok: '.wp-modal button.wp-btn--dialog.wp-btn--primary',
  cancel: '.wp-modal button.wp-btn--dialog:has-text("Cancel")',
} as const;

export const Revision = {
  /** ChangeDetailsViewImpl renders "R <n> ▾" as a clickable label when
   * revert/download are permitted — same hook 11-download D2 uses. */
  badge: /^R \d+ ▾$/,
  revertMenuItem: '.wp-popup-menu__item:has-text("Revert changes in revision")',
  /** MessageBox.showConfirmBox swaps the primary/escape button classes
   * (accept carries wp-btn--escape) — always match confirm buttons by
   * TEXT, never by wp-btn--primary. Every button selector below is scoped
   * to its own modal by caption: dismissing a wp-modal runs a 300ms fade
   * before it detaches, so the confirm box and the follow-up success box
   * coexist briefly, and an unscoped `.wp-modal button...primary` matches
   * buttons across both. */
  confirmModal: '.wp-modal:has-text("Revert Changes?")',
  confirmRevert: '.wp-modal:has-text("Revert Changes?") button:has-text("Revert")',
  /** Success MessageBox after a revert — must be dismissed, it would
   * otherwise intercept subsequent clicks. */
  successModal: '.wp-modal:has-text("have been reverted")',
  successOk:
    '.wp-modal:has-text("have been reverted") button.wp-btn--dialog.wp-btn--primary',
} as const;

export const ProjectMenu = {
  /** Top-bar "Project ▾" menu inside a project (ProjectMenuViewImpl). */
  button: 'button.wp-topbar__btn:has-text("Project")',
  /** Each MenuItem is an HTMLPanel (`.wp-popup-menu__item`) wrapping a
   * `.gwt-Label`. Match the container that HAS a label with the exact
   * text: a bare `.wp-popup-menu__item:text-is("Settings")` never matches
   * because Playwright's :text-is binds to the deepest text element (the
   * inner label), not the container. Scoping through the label keeps the
   * match exact so "Settings" doesn't also catch "Export settings...". */
  item: (label: string) =>
    `.wp-popup-menu__item:has(.gwt-Label:text-is("${label}"))`,
} as const;

export const SettingsPage = {
  /** All settings-style pages (tags/forms/prefixes/project settings/app
   * settings/perspectives manager) share SettingsViewImpl chrome. */
  root: '.wp-settings',
  section: (label: string) =>
    `.wp-settings__section:has(.wp-settings__section__title:text-is("${label}"))`,
  /** Labelled "Apply" on most pages, "OK" on Forms + Perspectives manager
   * (setApplyButtonText), so match the class not the text. */
  apply: 'button.wp-btn--page.wp-btn--accept',
  cancel: 'button.wp-btn--page.wp-btn--escape',
} as const;

export const Modal = {
  /** Generic wp-modal dialog chrome (ModalManager / MessageBox / InputBox). */
  root: '.wp-modal',
  primary: '.wp-modal button.wp-btn--dialog.wp-btn--primary',
  escape: '.wp-modal button.wp-btn--dialog.wp-btn--escape',
  /** InputBox single-line prompt renders a visible gwt-TextBox. */
  textInput: '.wp-modal input.gwt-TextBox',
  textArea: '.wp-modal textarea',
} as const;

export const PerspectiveBar = {
  /** "Tabs ▾" button at the right end of the perspective switcher
   * (PerspectiveSwitcherViewImpl newTabButton, msg.perspective_tabs). */
  tabsButton: 'button:has-text("Tabs")',
  /** Per-tab menu button — a MenuButton HTMLPanel with title="Menu"
   * (PerspectiveLinkImpl.ui.xml). Scoped under the tab so multiple tabs
   * don't collide. */
  tabMenuButton: (label: string) =>
    `${ProjectView.tab(label)} div[title="Menu"]`,
  selectedTab: '.gwt-TabBarItem-selected',
  /** Items: "Add view", "Reset" (built-ins only), "Close",
   * "Add blank tab…" (ellipsis char appended in code). */
  menuItem: (label: string) => `.wp-popup-menu__item:has-text("${label}")`,
  /** EmptyPerspectiveViewImpl renders this literal text; clicking it opens
   * the portlet chooser. */
  emptyPerspective: 'text=Click to add content',
  /** widgetmap ViewHolder caption for a dropped/added portlet. */
  viewCaption: (title: string) => `.widget-holder-label:text-is("${title}")`,
} as const;

export const PortletChooser = {
  /** "Choose view" is a legacy gwt-DialogBox (like the download format
   * chooser), containing a GWT ListBox of portlet titles. */
  dialog: '.gwt-DialogBox:has-text("Choose view")',
  list: '.gwt-DialogBox select',
  ok: '.gwt-DialogBox button.wp-btn--accept',
} as const;

export const TagsPage = {
  /** Project tags page (#projects/{id}/tags) — a ValueListFlexEditor of
   * TagEditor rows in AUTOMATIC mode (trailing blank row always present). */
  row: '.wp-value-list__row',
  labelInput: 'input[placeholder="Label"]',
  descriptionInput: 'input[placeholder="Description"]',
  deleteButton: '.wp-value-list__delete-button',
} as const;

export const EntityTags = {
  /** "Tags..." hierarchy context-menu item opens this modal
   * (EntityTagsSelectorViewImpl). Each row is an EntityTagCheckBoxImpl:
   * a gwt-CheckBox followed by the .wp-tag chip. */
  modal: '.wp-modal:has-text("Entity Tags")',
  checkboxFor: (tagLabel: string) =>
    `.wp-modal div:has(> .gwt-CheckBox):has(.wp-tag:has-text("${tagLabel}")) input[type="checkbox"]`,
  /** Assigned tags render as chips in the editor portlet's tag list. */
  assignedTag: (label: string) => `.wp-tag:has-text("${label}")`,
} as const;

export const PrefixesPage = {
  /** Prefix declarations page (#projects/{id}/prefixes). Validation:
   * prefix name must end with ':', prefix IRI must be absolute and end
   * with '/' or '#'. */
  row: '.wp-value-list__row',
  nameInput: 'input[placeholder="Prefix name"]',
  valueInput: 'input[placeholder="Prefix"]',
} as const;

export const FormsPage = {
  /** Forms manager (#projects/{id}/forms). The Apply button on this page
   * is relabelled "OK". Editing a form label fires updateForm on blur —
   * the form exists server-side even before OK. */
  addFormButton: 'button:has-text("Add form")',
  copyButton: 'button:has-text("Copy forms from project")',
  exportButton: 'button:has-text("Export forms...")',
  importButton: 'button:has-text("Import forms...")',
  formDetailsButton: 'button:has-text("Form details...")',
  labelValueInput: '.wp-value-list__row input.gwt-TextBox',
  /** The language tag sits next to the label in the same row as a GWT
   * SuggestBox — pressSequentially + Escape + Tab, never fill(). */
  labelLangInput: '.wp-value-list__row input.gwt-SuggestBox',
} as const;
