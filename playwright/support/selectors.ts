/**
 * Centralised Playwright selectors for the WebProtege GWT UI.
 *
 * Selector precedence:
 *   1. `[debug-id="..."]` — emitted by GWT once `com.google.gwt.user.Debug`
 *      is inherited (see webprotege-gwt-ui-client/src/main/module.gwt.xml).
 *      Stable across i18n changes and rebuilds.
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
  root: '[debug-id="TopBarContainer"]',
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
  root: 'role=dialog',
  name: 'input[type="text"]',
  description: 'textarea',
  language: '.formField input',
  fileUpload: 'input[type="file"]',
  submit: 'button:has-text("Create")',
  cancel: 'button:has-text("Cancel")',
  nameError: 'text=Please enter a project name',
} as const;

export const ProjectView = {
  root: '[debug-id="ProjectView"]',
  perspectiveSwitcher: '[debug-id="PerspectiveSwitcher"]',
  tab: (label: string) => `[debug-id="PerspectiveSwitcher"] >> text=${label}`,
  addTabButton: 'button:has-text("Tabs")',
} as const;

export const Hierarchy = {
  /** Generic tree-node locator by visible label. */
  treeNode: (label: string) => `.tree-content :text-is("${label}")`,
  selectedNode: '.tree-row-selected',
  toolbar: {
    create: 'button[title="Create"], button:has-text("Create")',
    delete: 'button[title="Delete"], button:has-text("Delete")',
    watch: 'button[title="Watch"]',
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
  root: 'role=dialog:has-text("Create")',
  name: 'role=dialog input[type="text"]',
  langTag: 'role=dialog select',
  submit: 'role=dialog button:has-text("Create")',
  cancel: 'role=dialog button:has-text("Cancel")',
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
