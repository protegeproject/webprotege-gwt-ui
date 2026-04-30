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
     * style class added in `*HierarchyPortletPresenter` instead. */
    create: 'button.wp-btn-g--create',
    delete: 'button.wp-btn-g--delete',
    watch: 'button.wp-btn-g--watch',
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
  shareWithList: '.value-list-flex-editor',
  topBarButton: 'button.wp-topbar__btn:has-text("Share")',
} as const;
