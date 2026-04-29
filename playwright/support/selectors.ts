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
  error: '.wp-login__message, #input-error',
} as const;

export const TopBar = {
  root: '#gwt-debug-TopBarContainer',
  projectTitle: '.wp-laf-topBarTitle',
  homeButton: '.wp-buttons-topBarButton:has-text("Home")',
  signOut: 'text=Sign Out',
  userMenu: '.wp-laf-topBar [class*="loggedInUser"]',
  helpLink: '[class*="helpContainer"] a',
} as const;

export const ProjectList = {
  root: '.wp-project-list',
  rows: '.wp-project-list__rows__row',
  nameCell: '.wp-project-list__name-col',
  ownerCell: '.wp-project-list__owner-col',
  lastOpenedCell: '.wp-project-list__last-opened-col',
  lastModifiedCell: '.wp-project-list__last-modified-col',
  menuButton: '.wp-project-list__menu-button-col button',
  filters: {
    ownedByMe: 'label:has-text("Owned by Me") input',
    sharedWithMe: 'label:has-text("Shared with Me") input',
    trash: 'label:has-text("Trash") input',
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
  tab: (label: string) => `#gwt-debug-PerspectiveSwitcher >> text=${label}`,
  addTabButton: 'button:has-text("Tabs")',
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
  contextMenu: 'role=menu',
  contextMenuItem: (label: string) => `role=menuitem >> text=${label}`,
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
  trigger: 'button[title*="Search"]',
  modal: 'role=dialog:has-text("Search")',
  input: 'role=dialog input[type="search"], role=dialog input[type="text"]',
  result: '.search-result, [class*="searchResult"]',
} as const;

export const Sharing = {
  dialog: 'role=dialog:has-text("Sharing")',
  linkSharingToggle: 'role=dialog input[type="checkbox"]',
  linkSharingPermission: 'role=dialog select',
  shareWithList: 'role=dialog .value-list-flex-editor',
} as const;
