# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: 20-navigation.spec.ts >> navigation >> NV3: "Show Direct Link" exposes a shareable URL
- Location: tests/20-navigation.spec.ts:72:7

# Error details

```
Error: expect(locator).toBeVisible() failed

Locator: locator('.gt-tree__row:has(:text-is("LinkTarget"))')
Expected: visible
Timeout: 15000ms
Error: element(s) not found

Call log:
  - Expect "toBeVisible" with timeout 15000ms
  - waiting for locator('.gt-tree__row:has(:text-is("LinkTarget"))')

```

# Page snapshot

```yaml
- generic [active] [ref=e1]:
  - iframe
  - iframe
  - iframe
  - generic [ref=e7]:
    - generic [ref=e10]:
      - generic [ref=e11]: Projects
      - button "e2e@test.local ▾" [ref=e14] [cursor=pointer]
    - generic [ref=e15]:
      - generic [ref=e16]:
        - button "Create New Project" [ref=e17] [cursor=pointer]
        - generic [ref=e19]:
          - generic [ref=e21]:
            - checkbox "Owned by Me" [checked] [ref=e22]
            - text: Owned by Me
          - generic [ref=e24]:
            - checkbox "Shared with Me" [checked] [ref=e25]
            - text: Shared with Me
          - generic [ref=e27]:
            - checkbox "Trash" [ref=e28]
            - text: Trash
        - combobox [ref=e29]:
          - option "Sort by Last Opened" [selected]
          - option "Sort by Last Modified"
          - option "Sort by Project Name"
          - option "Sort by Owner"
      - generic [ref=e30]:
        - generic [ref=e31]:
          - generic [ref=e32]: Project name
          - generic [ref=e33]: Owner
          - generic [ref=e34]: Last opened
          - generic [ref=e35]: Last modified
        - generic [ref=e36]:
          - generic [ref=e37]:
            - generic [ref=e38] [cursor=pointer]: Test_243dbc55
            - generic [ref=e39]:
              - generic [ref=e42]: E
              - generic [ref=e43]: e2e@test.local
            - generic [ref=e44]: Less than one minute ago
            - generic [ref=e45]: Less than one minute ago
            - generic "Menu" [ref=e46] [cursor=pointer]:
              - img [ref=e47]
          - generic [ref=e51]:
            - generic [ref=e52] [cursor=pointer]: Test_b62f2c46
            - generic [ref=e53]:
              - generic [ref=e56]: E
              - generic [ref=e57]: e2e@test.local
            - generic [ref=e58]: Less than one minute ago
            - generic [ref=e59]: Less than one minute ago
            - generic "Menu" [ref=e60] [cursor=pointer]:
              - img [ref=e61]
          - generic [ref=e65]:
            - generic [ref=e66] [cursor=pointer]: Test_613a6cdd
            - generic [ref=e67]:
              - generic [ref=e70]: E
              - generic [ref=e71]: e2e@test.local
            - generic [ref=e72]: Less than one minute ago
            - generic [ref=e73]: Less than one minute ago
            - generic "Menu" [ref=e74] [cursor=pointer]:
              - img [ref=e75]
          - generic [ref=e79]:
            - generic [ref=e80] [cursor=pointer]: Test_85e8da1e
            - generic [ref=e81]:
              - generic [ref=e84]: E
              - generic [ref=e85]: e2e@test.local
            - generic [ref=e86]: Less than one minute ago
            - generic [ref=e87]: Less than one minute ago
            - generic "Menu" [ref=e88] [cursor=pointer]:
              - img [ref=e89]
          - generic [ref=e93]:
            - generic [ref=e94] [cursor=pointer]: Test_8cb8af08
            - generic [ref=e95]:
              - generic [ref=e98]: E
              - generic [ref=e99]: e2e@test.local
            - generic [ref=e100]: Less than one minute ago
            - generic [ref=e101]: Less than one minute ago
            - generic "Menu" [ref=e102] [cursor=pointer]:
              - img [ref=e103]
          - generic [ref=e107]:
            - generic [ref=e108] [cursor=pointer]: Test_532ed14c
            - generic [ref=e109]:
              - generic [ref=e112]: E
              - generic [ref=e113]: e2e@test.local
            - generic [ref=e114]: One minute ago
            - generic [ref=e115]: One minute ago
            - generic "Menu" [ref=e116] [cursor=pointer]:
              - img [ref=e117]
          - generic [ref=e121]:
            - generic [ref=e122] [cursor=pointer]: Test_3d93c204
            - generic [ref=e123]:
              - generic [ref=e126]: E
              - generic [ref=e127]: e2e@test.local
            - generic [ref=e128]: One minute ago
            - generic [ref=e129]: One minute ago
            - generic "Menu" [ref=e130] [cursor=pointer]:
              - img [ref=e131]
          - generic [ref=e135]:
            - generic [ref=e136] [cursor=pointer]: Test_bc2e9b58
            - generic [ref=e137]:
              - generic [ref=e140]: E
              - generic [ref=e141]: e2e@test.local
            - generic [ref=e142]: One minute ago
            - generic [ref=e143]: One minute ago
            - generic "Menu" [ref=e144] [cursor=pointer]:
              - img [ref=e145]
          - generic [ref=e149]:
            - generic [ref=e150] [cursor=pointer]: Test_2522ecc8
            - generic [ref=e151]:
              - generic [ref=e154]: E
              - generic [ref=e155]: e2e@test.local
            - generic [ref=e156]: One minute ago
            - generic [ref=e157]: One minute ago
            - generic "Menu" [ref=e158] [cursor=pointer]:
              - img [ref=e159]
          - generic [ref=e163]:
            - generic [ref=e164] [cursor=pointer]: Test_fa48eb87
            - generic [ref=e165]:
              - generic [ref=e168]: E
              - generic [ref=e169]: e2e@test.local
            - generic [ref=e170]: One minute ago
            - generic [ref=e171]: One minute ago
            - generic "Menu" [ref=e172] [cursor=pointer]:
              - img [ref=e173]
          - generic [ref=e177]:
            - generic [ref=e178] [cursor=pointer]: Test_188b7459
            - generic [ref=e179]:
              - generic [ref=e182]: E
              - generic [ref=e183]: e2e@test.local
            - generic [ref=e184]: One minute ago
            - generic [ref=e185]: One minute ago
            - generic "Menu" [ref=e186] [cursor=pointer]:
              - img [ref=e187]
          - generic [ref=e191]:
            - generic [ref=e192] [cursor=pointer]: Test_d3aba6a4
            - generic [ref=e193]:
              - generic [ref=e196]: E
              - generic [ref=e197]: e2e@test.local
            - generic [ref=e198]: One minute ago
            - generic [ref=e199]: One minute ago
            - generic "Menu" [ref=e200] [cursor=pointer]:
              - img [ref=e201]
          - generic [ref=e205]:
            - generic [ref=e206] [cursor=pointer]: Test_7a66c84d
            - generic [ref=e207]:
              - generic [ref=e210]: E
              - generic [ref=e211]: e2e@test.local
            - generic [ref=e212]: One minute ago
            - generic [ref=e213]: One minute ago
            - generic "Menu" [ref=e214] [cursor=pointer]:
              - img [ref=e215]
          - generic [ref=e219]:
            - generic [ref=e220] [cursor=pointer]: Test_8acc7739
            - generic [ref=e221]:
              - generic [ref=e224]: E
              - generic [ref=e225]: e2e@test.local
            - generic [ref=e226]: One minute ago
            - generic [ref=e227]: One minute ago
            - generic "Menu" [ref=e228] [cursor=pointer]:
              - img [ref=e229]
          - generic [ref=e233]:
            - generic [ref=e234] [cursor=pointer]: Test_2cadf8eb
            - generic [ref=e235]:
              - generic [ref=e238]: E
              - generic [ref=e239]: e2e@test.local
            - generic [ref=e240]: One minute ago
            - generic [ref=e241]: One minute ago
            - generic "Menu" [ref=e242] [cursor=pointer]:
              - img [ref=e243]
          - generic [ref=e247]:
            - generic [ref=e248] [cursor=pointer]: Test_46e7f1ba
            - generic [ref=e249]:
              - generic [ref=e252]: E
              - generic [ref=e253]: e2e@test.local
            - generic [ref=e254]: 2 minutes ago
            - generic [ref=e255]: 2 minutes ago
            - generic "Menu" [ref=e256] [cursor=pointer]:
              - img [ref=e257]
          - generic [ref=e261]:
            - generic [ref=e262] [cursor=pointer]: Test_97023622
            - generic [ref=e263]:
              - generic [ref=e266]: E
              - generic [ref=e267]: e2e@test.local
            - generic [ref=e268]: 2 minutes ago
            - generic [ref=e269]: 2 minutes ago
            - generic "Menu" [ref=e270] [cursor=pointer]:
              - img [ref=e271]
          - generic [ref=e275]:
            - generic [ref=e276] [cursor=pointer]: Test_f3b8ee8d
            - generic [ref=e277]:
              - generic [ref=e280]: E
              - generic [ref=e281]: e2e@test.local
            - generic [ref=e282]: 2 minutes ago
            - generic [ref=e283]: 2 minutes ago
            - generic "Menu" [ref=e284] [cursor=pointer]:
              - img [ref=e285]
          - generic [ref=e289]:
            - generic [ref=e290] [cursor=pointer]: Test_d2b9030e
            - generic [ref=e291]:
              - generic [ref=e294]: E
              - generic [ref=e295]: e2e@test.local
            - generic [ref=e296]: 2 minutes ago
            - generic [ref=e297]: 2 minutes ago
            - generic "Menu" [ref=e298] [cursor=pointer]:
              - img [ref=e299]
          - generic [ref=e303]:
            - generic [ref=e304] [cursor=pointer]: Test_21f05b4a
            - generic [ref=e305]:
              - generic [ref=e308]: E
              - generic [ref=e309]: e2e@test.local
            - generic [ref=e310]: 2 minutes ago
            - generic [ref=e311]: 2 minutes ago
            - generic "Menu" [ref=e312] [cursor=pointer]:
              - img [ref=e313]
          - generic [ref=e317]:
            - generic [ref=e318] [cursor=pointer]: Test_21f5207c
            - generic [ref=e319]:
              - generic [ref=e322]: E
              - generic [ref=e323]: e2e@test.local
            - generic [ref=e324]: 2 minutes ago
            - generic [ref=e325]: 2 minutes ago
            - generic "Menu" [ref=e326] [cursor=pointer]:
              - img [ref=e327]
          - generic [ref=e331]:
            - generic [ref=e332] [cursor=pointer]: Test_6b93ad71
            - generic [ref=e333]:
              - generic [ref=e336]: E
              - generic [ref=e337]: e2e@test.local
            - generic [ref=e338]: 2 minutes ago
            - generic [ref=e339]: 2 minutes ago
            - generic "Menu" [ref=e340] [cursor=pointer]:
              - img [ref=e341]
          - generic [ref=e345]:
            - generic [ref=e346] [cursor=pointer]: Test_30bffc36
            - generic [ref=e347]:
              - generic [ref=e350]: E
              - generic [ref=e351]: e2e@test.local
            - generic [ref=e352]: 2 minutes ago
            - generic [ref=e353]: 2 minutes ago
            - generic "Menu" [ref=e354] [cursor=pointer]:
              - img [ref=e355]
          - generic [ref=e359]:
            - generic [ref=e360] [cursor=pointer]: Test_3be23448
            - generic [ref=e361]:
              - generic [ref=e364]: E
              - generic [ref=e365]: e2e@test.local
            - generic [ref=e366]: 2 minutes ago
            - generic [ref=e367]: 2 minutes ago
            - generic "Menu" [ref=e368] [cursor=pointer]:
              - img [ref=e369]
          - generic [ref=e373]:
            - generic [ref=e374] [cursor=pointer]: Test_82037de4
            - generic [ref=e375]:
              - generic [ref=e378]: E
              - generic [ref=e379]: e2e@test.local
            - generic [ref=e380]: 2 minutes ago
            - generic [ref=e381]: 2 minutes ago
            - generic "Menu" [ref=e382] [cursor=pointer]:
              - img [ref=e383]
          - generic [ref=e387]:
            - generic [ref=e388] [cursor=pointer]: Test_ca8f6967
            - generic [ref=e389]:
              - generic [ref=e392]: E
              - generic [ref=e393]: e2e@test.local
            - generic [ref=e394]: 2 minutes ago
            - generic [ref=e395]: 2 minutes ago
            - generic "Menu" [ref=e396] [cursor=pointer]:
              - img [ref=e397]
          - generic [ref=e401]:
            - generic [ref=e402] [cursor=pointer]: Test_c0e54ffc
            - generic [ref=e403]:
              - generic [ref=e406]: E
              - generic [ref=e407]: e2e@test.local
            - generic [ref=e408]: 2 minutes ago
            - generic [ref=e409]: 2 minutes ago
            - generic "Menu" [ref=e410] [cursor=pointer]:
              - img [ref=e411]
          - generic [ref=e415]:
            - generic [ref=e416] [cursor=pointer]: Test_18bed0c9
            - generic [ref=e417]:
              - generic [ref=e420]: E
              - generic [ref=e421]: e2e@test.local
            - generic [ref=e422]: 2 minutes ago
            - generic [ref=e423]: 3 minutes ago
            - generic "Menu" [ref=e424] [cursor=pointer]:
              - img [ref=e425]
          - generic [ref=e429]:
            - generic [ref=e430] [cursor=pointer]: Test_4a179649
            - generic [ref=e431]:
              - generic [ref=e434]: E
              - generic [ref=e435]: e2e@test.local
            - generic [ref=e436]: 2 minutes ago
            - generic [ref=e437]: 2 minutes ago
            - generic "Menu" [ref=e438] [cursor=pointer]:
              - img [ref=e439]
          - generic [ref=e443]:
            - generic [ref=e444] [cursor=pointer]: Test_c0fbf143
            - generic [ref=e445]:
              - generic [ref=e448]: E
              - generic [ref=e449]: e2e@test.local
            - generic [ref=e450]: 2 minutes ago
            - generic [ref=e451]: 2 minutes ago
            - generic "Menu" [ref=e452] [cursor=pointer]:
              - img [ref=e453]
          - generic [ref=e457]:
            - generic [ref=e458] [cursor=pointer]: Test_a5ca6e38
            - generic [ref=e459]:
              - generic [ref=e462]: E
              - generic [ref=e463]: e2e@test.local
            - generic [ref=e464]: 3 minutes ago
            - generic [ref=e465]: 3 minutes ago
            - generic "Menu" [ref=e466] [cursor=pointer]:
              - img [ref=e467]
          - generic [ref=e471]:
            - generic [ref=e472] [cursor=pointer]: Test_6ef419ef
            - generic [ref=e473]:
              - generic [ref=e476]: E
              - generic [ref=e477]: e2e@test.local
            - generic [ref=e478]: 3 minutes ago
            - generic [ref=e479]: 3 minutes ago
            - generic "Menu" [ref=e480] [cursor=pointer]:
              - img [ref=e481]
          - generic [ref=e485]:
            - generic [ref=e486] [cursor=pointer]: Test_395776b1
            - generic [ref=e487]:
              - generic [ref=e490]: E
              - generic [ref=e491]: e2e@test.local
            - generic [ref=e492]: 3 minutes ago
            - generic [ref=e493]: 3 minutes ago
            - generic "Menu" [ref=e494] [cursor=pointer]:
              - img [ref=e495]
          - generic [ref=e499]:
            - generic [ref=e500] [cursor=pointer]: Test_520b9428
            - generic [ref=e501]:
              - generic [ref=e504]: E
              - generic [ref=e505]: e2e@test.local
            - generic [ref=e506]: 3 minutes ago
            - generic [ref=e507]: 3 minutes ago
            - generic "Menu" [ref=e508] [cursor=pointer]:
              - img [ref=e509]
          - generic [ref=e513]:
            - generic [ref=e514] [cursor=pointer]: Test_b938aa41
            - generic [ref=e515]:
              - generic [ref=e518]: E
              - generic [ref=e519]: e2e@test.local
            - generic [ref=e520]: 3 minutes ago
            - generic [ref=e521]: 3 minutes ago
            - generic "Menu" [ref=e522] [cursor=pointer]:
              - img [ref=e523]
          - generic [ref=e527]:
            - generic [ref=e528] [cursor=pointer]: Test_f432a145
            - generic [ref=e529]:
              - generic [ref=e532]: E
              - generic [ref=e533]: e2e@test.local
            - generic [ref=e534]: 3 minutes ago
            - generic [ref=e535]: 3 minutes ago
            - generic "Menu" [ref=e536] [cursor=pointer]:
              - img [ref=e537]
          - generic [ref=e541]:
            - generic [ref=e542] [cursor=pointer]: Test_becb6fa6
            - generic [ref=e543]:
              - generic [ref=e546]: E
              - generic [ref=e547]: e2e@test.local
            - generic [ref=e548]: 3 minutes ago
            - generic [ref=e549]: 3 minutes ago
            - generic "Menu" [ref=e550] [cursor=pointer]:
              - img [ref=e551]
          - generic [ref=e555]:
            - generic [ref=e556] [cursor=pointer]: Test_09b0e21c
            - generic [ref=e557]:
              - generic [ref=e560]: E
              - generic [ref=e561]: e2e@test.local
            - generic [ref=e562]: 3 minutes ago
            - generic [ref=e563]: 3 minutes ago
            - generic "Menu" [ref=e564] [cursor=pointer]:
              - img [ref=e565]
          - generic [ref=e569]:
            - generic [ref=e570] [cursor=pointer]: Test_3d0a577c
            - generic [ref=e571]:
              - generic [ref=e574]: E
              - generic [ref=e575]: e2e@test.local
            - generic [ref=e576]: 3 minutes ago
            - generic [ref=e577]: 3 minutes ago
            - generic "Menu" [ref=e578] [cursor=pointer]:
              - img [ref=e579]
          - generic [ref=e583]:
            - generic [ref=e584] [cursor=pointer]: Test_1bfb3a27
            - generic [ref=e585]:
              - generic [ref=e588]: E
              - generic [ref=e589]: e2e@test.local
            - generic [ref=e590]: 3 minutes ago
            - generic [ref=e591]: 3 minutes ago
            - generic "Menu" [ref=e592] [cursor=pointer]:
              - img [ref=e593]
          - generic [ref=e597]:
            - generic [ref=e598] [cursor=pointer]: Test_dbe9ba94
            - generic [ref=e599]:
              - generic [ref=e602]: E
              - generic [ref=e603]: e2e@test.local
            - generic [ref=e604]: 3 minutes ago
            - generic [ref=e605]: 3 minutes ago
            - generic "Menu" [ref=e606] [cursor=pointer]:
              - img [ref=e607]
          - generic [ref=e611]:
            - generic [ref=e612] [cursor=pointer]: Test_90b9c738
            - generic [ref=e613]:
              - generic [ref=e616]: E
              - generic [ref=e617]: e2e@test.local
            - generic [ref=e618]: 4 minutes ago
            - generic [ref=e619]: 4 minutes ago
            - generic "Menu" [ref=e620] [cursor=pointer]:
              - img [ref=e621]
          - generic [ref=e625]:
            - generic [ref=e626] [cursor=pointer]: Test_b8e8f59d
            - generic [ref=e627]:
              - generic [ref=e630]: E
              - generic [ref=e631]: e2e@test.local
            - generic [ref=e632]: 4 minutes ago
            - generic [ref=e633]: 4 minutes ago
            - generic "Menu" [ref=e634] [cursor=pointer]:
              - img [ref=e635]
          - generic [ref=e639]:
            - generic [ref=e640] [cursor=pointer]: Test_d8506d46
            - generic [ref=e641]:
              - generic [ref=e644]: E
              - generic [ref=e645]: e2e@test.local
            - generic [ref=e646]: 4 minutes ago
            - generic [ref=e647]: 4 minutes ago
            - generic "Menu" [ref=e648] [cursor=pointer]:
              - img [ref=e649]
          - generic [ref=e653]:
            - generic [ref=e654] [cursor=pointer]: Test_f93ee748
            - generic [ref=e655]:
              - generic [ref=e658]: E
              - generic [ref=e659]: e2e@test.local
            - generic [ref=e660]: 4 minutes ago
            - generic [ref=e661]: 4 minutes ago
            - generic "Menu" [ref=e662] [cursor=pointer]:
              - img [ref=e663]
          - generic [ref=e667]:
            - generic [ref=e668] [cursor=pointer]: Test_ff763953
            - generic [ref=e669]:
              - generic [ref=e672]: E
              - generic [ref=e673]: e2e@test.local
            - generic [ref=e674]: 4 minutes ago
            - generic [ref=e675]: 4 minutes ago
            - generic "Menu" [ref=e676] [cursor=pointer]:
              - img [ref=e677]
          - generic [ref=e681]:
            - generic [ref=e682] [cursor=pointer]: Test_8e26057e
            - generic [ref=e683]:
              - generic [ref=e686]: E
              - generic [ref=e687]: e2e@test.local
            - generic [ref=e688]: 4 minutes ago
            - generic [ref=e689]: 4 minutes ago
            - generic "Menu" [ref=e690] [cursor=pointer]:
              - img [ref=e691]
          - generic [ref=e695]:
            - generic [ref=e696] [cursor=pointer]: Test_1ab3e2f0
            - generic [ref=e697]:
              - generic [ref=e700]: E
              - generic [ref=e701]: e2e@test.local
            - generic [ref=e702]: 4 minutes ago
            - generic [ref=e703]: 4 minutes ago
            - generic "Menu" [ref=e704] [cursor=pointer]:
              - img [ref=e705]
          - generic [ref=e709]:
            - generic [ref=e710] [cursor=pointer]: Test_f981b7e3
            - generic [ref=e711]:
              - generic [ref=e714]: E
              - generic [ref=e715]: e2e@test.local
            - generic [ref=e716]: 4 minutes ago
            - generic [ref=e717]: 4 minutes ago
            - generic "Menu" [ref=e718] [cursor=pointer]:
              - img [ref=e719]
          - generic [ref=e723]:
            - generic [ref=e724] [cursor=pointer]: Test_732cd5a3
            - generic [ref=e725]:
              - generic [ref=e728]: E
              - generic [ref=e729]: e2e@test.local
            - generic [ref=e730]: 4 minutes ago
            - generic [ref=e731]: 4 minutes ago
            - generic "Menu" [ref=e732] [cursor=pointer]:
              - img [ref=e733]
          - generic [ref=e737]:
            - generic [ref=e738] [cursor=pointer]: Test_d04d5422
            - generic [ref=e739]:
              - generic [ref=e742]: E
              - generic [ref=e743]: e2e@test.local
            - generic [ref=e744]: 4 minutes ago
            - generic [ref=e745]: 4 minutes ago
            - generic "Menu" [ref=e746] [cursor=pointer]:
              - img [ref=e747]
          - generic [ref=e751]:
            - generic [ref=e752] [cursor=pointer]: Test_9dc6f05c
            - generic [ref=e753]:
              - generic [ref=e756]: E
              - generic [ref=e757]: e2e@test.local
            - generic [ref=e758]: 4 minutes ago
            - generic [ref=e759]: 4 minutes ago
            - generic "Menu" [ref=e760] [cursor=pointer]:
              - img [ref=e761]
          - generic [ref=e765]:
            - generic [ref=e766] [cursor=pointer]: Test_64de6124
            - generic [ref=e767]:
              - generic [ref=e770]: E
              - generic [ref=e771]: e2e@test.local
            - generic [ref=e772]: 4 minutes ago
            - generic [ref=e773]: 4 minutes ago
            - generic "Menu" [ref=e774] [cursor=pointer]:
              - img [ref=e775]
          - generic [ref=e779]:
            - generic [ref=e780] [cursor=pointer]: Test_eca1879d
            - generic [ref=e781]:
              - generic [ref=e784]: E
              - generic [ref=e785]: e2e@test.local
            - generic [ref=e786]: 5 minutes ago
            - generic [ref=e787]: 5 minutes ago
            - generic "Menu" [ref=e788] [cursor=pointer]:
              - img [ref=e789]
          - generic [ref=e793]:
            - generic [ref=e794] [cursor=pointer]: Test_93c2ddd7
            - generic [ref=e795]:
              - generic [ref=e798]: E
              - generic [ref=e799]: e2e@test.local
            - generic [ref=e800]: 5 minutes ago
            - generic [ref=e801]: 5 minutes ago
            - generic "Menu" [ref=e802] [cursor=pointer]:
              - img [ref=e803]
          - generic [ref=e807]:
            - generic [ref=e808] [cursor=pointer]: Test_abe60169
            - generic [ref=e809]:
              - generic [ref=e812]: E
              - generic [ref=e813]: e2e@test.local
            - generic [ref=e814]: 5 minutes ago
            - generic [ref=e815]: 5 minutes ago
            - generic "Menu" [ref=e816] [cursor=pointer]:
              - img [ref=e817]
          - generic [ref=e821]:
            - generic [ref=e822] [cursor=pointer]: Test_60bfa1f0
            - generic [ref=e823]:
              - generic [ref=e826]: E
              - generic [ref=e827]: e2e@test.local
            - generic [ref=e828]: 5 minutes ago
            - generic [ref=e829]: 5 minutes ago
            - generic "Menu" [ref=e830] [cursor=pointer]:
              - img [ref=e831]
          - generic [ref=e835]:
            - generic [ref=e836] [cursor=pointer]: Test_834e37ca
            - generic [ref=e837]:
              - generic [ref=e840]: E
              - generic [ref=e841]: e2e@test.local
            - generic [ref=e842]: 5 minutes ago
            - generic [ref=e843]: 5 minutes ago
            - generic "Menu" [ref=e844] [cursor=pointer]:
              - img [ref=e845]
          - generic [ref=e849]:
            - generic [ref=e850] [cursor=pointer]: Test_aecaa7cc
            - generic [ref=e851]:
              - generic [ref=e854]: E
              - generic [ref=e855]: e2e@test.local
            - generic [ref=e856]: 5 minutes ago
            - generic [ref=e857]: 5 minutes ago
            - generic "Menu" [ref=e858] [cursor=pointer]:
              - img [ref=e859]
          - generic [ref=e863]:
            - generic [ref=e864] [cursor=pointer]: Test_8b0b4072
            - generic [ref=e865]:
              - generic [ref=e868]: E
              - generic [ref=e869]: e2e@test.local
            - generic [ref=e870]: 5 minutes ago
            - generic [ref=e871]: 5 minutes ago
            - generic "Menu" [ref=e872] [cursor=pointer]:
              - img [ref=e873]
          - generic [ref=e877]:
            - generic [ref=e878] [cursor=pointer]: Test_d5d306f7
            - generic [ref=e879]:
              - generic [ref=e882]: E
              - generic [ref=e883]: e2e@test.local
            - generic [ref=e884]: 5 minutes ago
            - generic [ref=e885]: 5 minutes ago
            - generic "Menu" [ref=e886] [cursor=pointer]:
              - img [ref=e887]
          - generic [ref=e891]:
            - generic [ref=e892] [cursor=pointer]: Test_3f5ec743
            - generic [ref=e893]:
              - generic [ref=e896]: E
              - generic [ref=e897]: e2e@test.local
            - generic [ref=e898]: 6 minutes ago
            - generic [ref=e899]: 6 minutes ago
            - generic "Menu" [ref=e900] [cursor=pointer]:
              - img [ref=e901]
          - generic [ref=e905]:
            - generic [ref=e906] [cursor=pointer]: Test_ffa63eae
            - generic [ref=e907]:
              - generic [ref=e910]: E
              - generic [ref=e911]: e2e@test.local
            - generic [ref=e912]: 6 minutes ago
            - generic [ref=e913]: 6 minutes ago
            - generic "Menu" [ref=e914] [cursor=pointer]:
              - img [ref=e915]
          - generic [ref=e919]:
            - generic [ref=e920] [cursor=pointer]: Test_ad229698
            - generic [ref=e921]:
              - generic [ref=e924]: E
              - generic [ref=e925]: e2e@test.local
            - generic [ref=e926]: 6 minutes ago
            - generic [ref=e927]: 6 minutes ago
            - generic "Menu" [ref=e928] [cursor=pointer]:
              - img [ref=e929]
          - generic [ref=e933]:
            - generic [ref=e934] [cursor=pointer]: Test_85c7c173
            - generic [ref=e935]:
              - generic [ref=e938]: E
              - generic [ref=e939]: e2e@test.local
            - generic [ref=e940]: 6 minutes ago
            - generic [ref=e941]: 6 minutes ago
            - generic "Menu" [ref=e942] [cursor=pointer]:
              - img [ref=e943]
          - generic [ref=e947]:
            - generic [ref=e948] [cursor=pointer]: Test_5ab3eefd
            - generic [ref=e949]:
              - generic [ref=e952]: E
              - generic [ref=e953]: e2e@test.local
            - generic [ref=e954]: 6 minutes ago
            - generic [ref=e955]: 6 minutes ago
            - generic "Menu" [ref=e956] [cursor=pointer]:
              - img [ref=e957]
          - generic [ref=e961]:
            - generic [ref=e962] [cursor=pointer]: Test_b7f89a56
            - generic [ref=e963]:
              - generic [ref=e966]: E
              - generic [ref=e967]: e2e@test.local
            - generic [ref=e968]: 6 minutes ago
            - generic [ref=e969]: 6 minutes ago
            - generic "Menu" [ref=e970] [cursor=pointer]:
              - img [ref=e971]
          - generic [ref=e975]:
            - generic [ref=e976] [cursor=pointer]: Test_56004586
            - generic [ref=e977]:
              - generic [ref=e980]: E
              - generic [ref=e981]: e2e@test.local
            - generic [ref=e982]: 6 minutes ago
            - generic [ref=e983]: 6 minutes ago
            - generic "Menu" [ref=e984] [cursor=pointer]:
              - img [ref=e985]
          - generic [ref=e989]:
            - generic [ref=e990] [cursor=pointer]: Test_8811c8f0
            - generic [ref=e991]:
              - generic [ref=e994]: E
              - generic [ref=e995]: e2e@test.local
            - generic [ref=e996]: 6 minutes ago
            - generic [ref=e997]: 6 minutes ago
            - generic "Menu" [ref=e998] [cursor=pointer]:
              - img [ref=e999]
          - generic [ref=e1003]:
            - generic [ref=e1004] [cursor=pointer]: Test_99214256
            - generic [ref=e1005]:
              - generic [ref=e1008]: E
              - generic [ref=e1009]: e2e@test.local
            - generic [ref=e1010]: 7 minutes ago
            - generic [ref=e1011]: 7 minutes ago
            - generic "Menu" [ref=e1012] [cursor=pointer]:
              - img [ref=e1013]
          - generic [ref=e1017]:
            - generic [ref=e1018] [cursor=pointer]: Test_5988838b
            - generic [ref=e1019]:
              - generic [ref=e1022]: E
              - generic [ref=e1023]: e2e@test.local
            - generic [ref=e1024]: 7 minutes ago
            - generic [ref=e1025]: 7 minutes ago
            - generic "Menu" [ref=e1026] [cursor=pointer]:
              - img [ref=e1027]
          - generic [ref=e1031]:
            - generic [ref=e1032] [cursor=pointer]: Test_7d91a541
            - generic [ref=e1033]:
              - generic [ref=e1036]: E
              - generic [ref=e1037]: e2e@test.local
            - generic [ref=e1038]: 7 minutes ago
            - generic [ref=e1039]: 7 minutes ago
            - generic "Menu" [ref=e1040] [cursor=pointer]:
              - img [ref=e1041]
          - generic [ref=e1045]:
            - generic [ref=e1046] [cursor=pointer]: Test_1ab795e9
            - generic [ref=e1047]:
              - generic [ref=e1050]: E
              - generic [ref=e1051]: e2e@test.local
            - generic [ref=e1052]: 7 minutes ago
            - generic [ref=e1053]: 7 minutes ago
            - generic "Menu" [ref=e1054] [cursor=pointer]:
              - img [ref=e1055]
          - generic [ref=e1059]:
            - generic [ref=e1060] [cursor=pointer]: Test_d1c057ae
            - generic [ref=e1061]:
              - generic [ref=e1064]: E
              - generic [ref=e1065]: e2e@test.local
            - generic [ref=e1066]: 7 minutes ago
            - generic [ref=e1067]: 7 minutes ago
            - generic "Menu" [ref=e1068] [cursor=pointer]:
              - img [ref=e1069]
          - generic [ref=e1073]:
            - generic [ref=e1074] [cursor=pointer]: Test_5d3c7c5e
            - generic [ref=e1075]:
              - generic [ref=e1078]: E
              - generic [ref=e1079]: e2e@test.local
            - generic [ref=e1080]: 7 minutes ago
            - generic [ref=e1081]: 7 minutes ago
            - generic "Menu" [ref=e1082] [cursor=pointer]:
              - img [ref=e1083]
          - generic [ref=e1087]:
            - generic [ref=e1088] [cursor=pointer]: Test_00ed1460
            - generic [ref=e1089]:
              - generic [ref=e1092]: E
              - generic [ref=e1093]: e2e@test.local
            - generic [ref=e1094]: 7 minutes ago
            - generic [ref=e1095]: 7 minutes ago
            - generic "Menu" [ref=e1096] [cursor=pointer]:
              - img [ref=e1097]
          - generic [ref=e1101]:
            - generic [ref=e1102] [cursor=pointer]: Test_8330aeef
            - generic [ref=e1103]:
              - generic [ref=e1106]: E
              - generic [ref=e1107]: e2e@test.local
            - generic [ref=e1108]: 7 minutes ago
            - generic [ref=e1109]: 7 minutes ago
            - generic "Menu" [ref=e1110] [cursor=pointer]:
              - img [ref=e1111]
          - generic [ref=e1115]:
            - generic [ref=e1116] [cursor=pointer]: Test_0d36d810
            - generic [ref=e1117]:
              - generic [ref=e1120]: E
              - generic [ref=e1121]: e2e@test.local
            - generic [ref=e1122]: 7 minutes ago
            - generic [ref=e1123]: 7 minutes ago
            - generic "Menu" [ref=e1124] [cursor=pointer]:
              - img [ref=e1125]
          - generic [ref=e1129]:
            - generic [ref=e1130] [cursor=pointer]: Test_d5dd504f
            - generic [ref=e1131]:
              - generic [ref=e1134]: E
              - generic [ref=e1135]: e2e@test.local
            - generic [ref=e1136]: 8 minutes ago
            - generic [ref=e1137]: 8 minutes ago
            - generic "Menu" [ref=e1138] [cursor=pointer]:
              - img [ref=e1139]
          - generic [ref=e1143]:
            - generic [ref=e1144] [cursor=pointer]: Test_c16afa33
            - generic [ref=e1145]:
              - generic [ref=e1148]: E
              - generic [ref=e1149]: e2e@test.local
            - generic [ref=e1150]: 8 minutes ago
            - generic [ref=e1151]: 8 minutes ago
            - generic "Menu" [ref=e1152] [cursor=pointer]:
              - img [ref=e1153]
          - generic [ref=e1157]:
            - generic [ref=e1158] [cursor=pointer]: Test_82437682
            - generic [ref=e1159]:
              - generic [ref=e1162]: E
              - generic [ref=e1163]: e2e@test.local
            - generic [ref=e1164]: 8 minutes ago
            - generic [ref=e1165]: 8 minutes ago
            - generic "Menu" [ref=e1166] [cursor=pointer]:
              - img [ref=e1167]
          - generic [ref=e1171]:
            - generic [ref=e1172] [cursor=pointer]: Test_41165246
            - generic [ref=e1173]:
              - generic [ref=e1176]: E
              - generic [ref=e1177]: e2e@test.local
            - generic [ref=e1178]: 8 minutes ago
            - generic [ref=e1179]: 8 minutes ago
            - generic "Menu" [ref=e1180] [cursor=pointer]:
              - img [ref=e1181]
          - generic [ref=e1185]:
            - generic [ref=e1186] [cursor=pointer]: Test_fd6b6251
            - generic [ref=e1187]:
              - generic [ref=e1190]: E
              - generic [ref=e1191]: e2e@test.local
            - generic [ref=e1192]: 8 minutes ago
            - generic [ref=e1193]: 8 minutes ago
            - generic "Menu" [ref=e1194] [cursor=pointer]:
              - img [ref=e1195]
          - generic [ref=e1199]:
            - generic [ref=e1200] [cursor=pointer]: Test_8dcd5203
            - generic [ref=e1201]:
              - generic [ref=e1204]: E
              - generic [ref=e1205]: e2e@test.local
            - generic [ref=e1206]: 8 minutes ago
            - generic [ref=e1207]: 8 minutes ago
            - generic "Menu" [ref=e1208] [cursor=pointer]:
              - img [ref=e1209]
          - generic [ref=e1213]:
            - generic [ref=e1214] [cursor=pointer]: Test_a96bff7d
            - generic [ref=e1215]:
              - generic [ref=e1218]: E
              - generic [ref=e1219]: e2e@test.local
            - generic [ref=e1220]: 9 minutes ago
            - generic [ref=e1221]: 8 minutes ago
            - generic "Menu" [ref=e1222] [cursor=pointer]:
              - img [ref=e1223]
          - generic [ref=e1227]:
            - generic [ref=e1228] [cursor=pointer]: Test_c0640800
            - generic [ref=e1229]:
              - generic [ref=e1232]: E
              - generic [ref=e1233]: e2e@test.local
            - generic [ref=e1234]: 9 minutes ago
            - generic [ref=e1235]: 9 minutes ago
            - generic "Menu" [ref=e1236] [cursor=pointer]:
              - img [ref=e1237]
          - generic [ref=e1241]:
            - generic [ref=e1242] [cursor=pointer]: Test_97123dd9
            - generic [ref=e1243]:
              - generic [ref=e1246]: E
              - generic [ref=e1247]: e2e@test.local
            - generic [ref=e1248]: 9 minutes ago
            - generic [ref=e1249]: 9 minutes ago
            - generic "Menu" [ref=e1250] [cursor=pointer]:
              - img [ref=e1251]
          - generic [ref=e1255]:
            - generic [ref=e1256] [cursor=pointer]: Test_e41d154c
            - generic [ref=e1257]:
              - generic [ref=e1260]: E
              - generic [ref=e1261]: e2e@test.local
            - generic [ref=e1262]: 9 minutes ago
            - generic [ref=e1263]: 9 minutes ago
            - generic "Menu" [ref=e1264] [cursor=pointer]:
              - img [ref=e1265]
          - generic [ref=e1269]:
            - generic [ref=e1270] [cursor=pointer]: Test_4d02645a
            - generic [ref=e1271]:
              - generic [ref=e1274]: E
              - generic [ref=e1275]: e2e@test.local
            - generic [ref=e1276]: 9 minutes ago
            - generic [ref=e1277]: 9 minutes ago
            - generic "Menu" [ref=e1278] [cursor=pointer]:
              - img [ref=e1279]
          - generic [ref=e1283]:
            - generic [ref=e1284] [cursor=pointer]: Test_22e67ce0
            - generic [ref=e1285]:
              - generic [ref=e1288]: E
              - generic [ref=e1289]: e2e@test.local
            - generic [ref=e1290]: 10 minutes ago
            - generic [ref=e1291]: 9 minutes ago
            - generic "Menu" [ref=e1292] [cursor=pointer]:
              - img [ref=e1293]
          - generic [ref=e1297]:
            - generic [ref=e1298] [cursor=pointer]: Test_d1f92f6a
            - generic [ref=e1299]:
              - generic [ref=e1302]: E
              - generic [ref=e1303]: e2e@test.local
            - generic [ref=e1304]: 10 minutes ago
            - generic [ref=e1305]: 10 minutes ago
            - generic "Menu" [ref=e1306] [cursor=pointer]:
              - img [ref=e1307]
          - generic [ref=e1311]:
            - generic [ref=e1312] [cursor=pointer]: Test_6a0dcad3
            - generic [ref=e1313]:
              - generic [ref=e1316]: E
              - generic [ref=e1317]: e2e@test.local
            - generic [ref=e1318]: 10 minutes ago
            - generic [ref=e1319]: 10 minutes ago
            - generic "Menu" [ref=e1320] [cursor=pointer]:
              - img [ref=e1321]
          - generic [ref=e1325]:
            - generic [ref=e1326] [cursor=pointer]: Test_8daa1762
            - generic [ref=e1327]:
              - generic [ref=e1330]: E
              - generic [ref=e1331]: e2e@test.local
            - generic [ref=e1332]: 10 minutes ago
            - generic [ref=e1333]: 10 minutes ago
            - generic "Menu" [ref=e1334] [cursor=pointer]:
              - img [ref=e1335]
          - generic [ref=e1339]:
            - generic [ref=e1340] [cursor=pointer]: Test_b6848ca7
            - generic [ref=e1341]:
              - generic [ref=e1344]: E
              - generic [ref=e1345]: e2e@test.local
            - generic [ref=e1346]: 10 minutes ago
            - generic [ref=e1347]: 10 minutes ago
            - generic "Menu" [ref=e1348] [cursor=pointer]:
              - img [ref=e1349]
          - generic [ref=e1353]:
            - generic [ref=e1354] [cursor=pointer]: Test_82f373c4
            - generic [ref=e1355]:
              - generic [ref=e1358]: E
              - generic [ref=e1359]: e2e@test.local
            - generic [ref=e1360]: 10 minutes ago
            - generic [ref=e1361]: 10 minutes ago
            - generic "Menu" [ref=e1362] [cursor=pointer]:
              - img [ref=e1363]
          - generic [ref=e1367]:
            - generic [ref=e1368] [cursor=pointer]: RoundTrip_358a43f2
            - generic [ref=e1369]:
              - generic [ref=e1372]: E
              - generic [ref=e1373]: e2e@test.local
            - generic [ref=e1374]: 10 minutes ago
            - generic [ref=e1375]: 10 minutes ago
            - generic "Menu" [ref=e1376] [cursor=pointer]:
              - img [ref=e1377]
          - generic [ref=e1381]:
            - generic [ref=e1382] [cursor=pointer]: Test_4826e1fa
            - generic [ref=e1383]:
              - generic [ref=e1386]: E
              - generic [ref=e1387]: e2e@test.local
            - generic [ref=e1388]: 10 minutes ago
            - generic [ref=e1389]: 10 minutes ago
            - generic "Menu" [ref=e1390] [cursor=pointer]:
              - img [ref=e1391]
          - generic [ref=e1395]:
            - generic [ref=e1396] [cursor=pointer]: Test_7a59546e
            - generic [ref=e1397]:
              - generic [ref=e1400]: E
              - generic [ref=e1401]: e2e@test.local
            - generic [ref=e1402]: 10 minutes ago
            - generic [ref=e1403]: 10 minutes ago
            - generic "Menu" [ref=e1404] [cursor=pointer]:
              - img [ref=e1405]
          - generic [ref=e1409]:
            - generic [ref=e1410] [cursor=pointer]: Test_f4f6fa2b
            - generic [ref=e1411]:
              - generic [ref=e1414]: E
              - generic [ref=e1415]: e2e@test.local
            - generic [ref=e1416]: 10 minutes ago
            - generic [ref=e1417]: 10 minutes ago
            - generic "Menu" [ref=e1418] [cursor=pointer]:
              - img [ref=e1419]
          - generic [ref=e1423]:
            - generic [ref=e1424] [cursor=pointer]: Test_04d05af0
            - generic [ref=e1425]:
              - generic [ref=e1428]: E
              - generic [ref=e1429]: e2e@test.local
            - generic [ref=e1431]: 6 minutes ago
            - generic "Menu" [ref=e1432] [cursor=pointer]:
              - img [ref=e1433]
          - generic [ref=e1437]:
            - generic [ref=e1438] [cursor=pointer]: Test_157e76ae
            - generic [ref=e1439]:
              - generic [ref=e1442]: E
              - generic [ref=e1443]: e2e@test.local
            - generic [ref=e1445]: 10 minutes ago
            - generic "Menu" [ref=e1446] [cursor=pointer]:
              - img [ref=e1447]
          - generic [ref=e1451]:
            - generic [ref=e1452] [cursor=pointer]: Test_5e1a3987
            - generic [ref=e1453]:
              - generic [ref=e1456]: E
              - generic [ref=e1457]: e2e@test.local
            - generic [ref=e1459]: 2 minutes ago
            - generic "Menu" [ref=e1460] [cursor=pointer]:
              - img [ref=e1461]
```

# Test source

```ts
  1   | import { Page } from '@playwright/test';
  2   | import { test, expect } from '../support/fixtures';
  3   | import {
  4   |   CreateEntityDialog,
  5   |   FrameEditor,
  6   |   Hierarchy,
  7   |   Modal,
  8   |   ProjectList,
  9   |   ProjectView,
  10  |   TopBar,
  11  | } from '../support/selectors';
  12  | 
  13  | /**
  14  |  * Deep linking and navigation. ProjectViewPlaceTokenizer serializes the
  15  |  * selection as `?selection=Type(content)` appended to the perspective
  16  |  * hash route; `owl:` is the only registered prefix for prefixed names,
  17  |  * generated IRIs appear angle-bracket-quoted (and percent-encoded in
  18  |  * page.url()).
  19  |  */
  20  | 
  21  | async function createClassUnder(
  22  |   page: Page,
  23  |   parentLabel: string,
  24  |   newLabel: string,
  25  | ): Promise<void> {
  26  |   await page.locator(Hierarchy.treeNode(parentLabel)).first().click();
  27  |   await page.locator(Hierarchy.toolbar.create).first().click();
  28  |   await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
  29  |   await page.locator(CreateEntityDialog.name).fill(newLabel);
  30  |   await page.locator(CreateEntityDialog.submit).click();
> 31  |   await expect(page.locator(Hierarchy.treeNode(newLabel))).toBeVisible({
      |                                                            ^ Error: expect(locator).toBeVisible() failed
  32  |     timeout: 15_000,
  33  |   });
  34  | }
  35  | 
  36  | test.describe('navigation', () => {
  37  |   test('NV1: a static deep link with a prefixed-name token selects owl:Thing', async ({
  38  |     page,
  39  |     project,
  40  |   }) => {
  41  |     await page.goto(`${project.url}?selection=Class(owl:Thing)`);
  42  |     await expect(
  43  |       page.locator(Hierarchy.selectedNode).filter({ hasText: 'owl:Thing' }),
  44  |     ).toBeVisible({ timeout: 30_000 });
  45  |     await expect(page.locator(FrameEditor.annotationsSection)).toBeVisible({
  46  |       timeout: 15_000,
  47  |     });
  48  |   });
  49  | 
  50  |   test('NV2: the selection URL round-trips as a cold-load deep link', async ({
  51  |     page,
  52  |     project,
  53  |   }) => {
  54  |     await createClassUnder(page, 'owl:Thing', 'DeepLinkTarget');
  55  |     await page.locator(Hierarchy.treeNode('DeepLinkTarget')).first().click();
  56  |     await page.waitForURL(/\?selection=Class\(/, { timeout: 15_000 });
  57  |     const deepLink = page.url();
  58  | 
  59  |     // Force a genuine cold GWT bootstrap rather than a hashchange.
  60  |     await page.goto('about:blank');
  61  |     await page.goto(deepLink);
  62  | 
  63  |     await expect(page.locator(ProjectView.root)).toBeVisible({ timeout: 30_000 });
  64  |     // graphtree reveals the ancestors of the selection key, so the node
  65  |     // is both present and marked selected once the tree settles.
  66  |     await expect(
  67  |       page.locator(Hierarchy.selectedNode).filter({ hasText: 'DeepLinkTarget' }),
  68  |     ).toBeVisible({ timeout: 15_000 });
  69  |     await expect(page.locator(FrameEditor.annotationsSection)).toBeVisible();
  70  |   });
  71  | 
  72  |   test('NV3: "Show Direct Link" exposes a shareable URL', async ({
  73  |     page,
  74  |     project,
  75  |   }) => {
  76  |     await createClassUnder(page, 'owl:Thing', 'LinkTarget');
  77  |     await page.locator(Hierarchy.treeNode('LinkTarget')).first().click();
  78  |     await page.locator(Hierarchy.treeNode('LinkTarget')).first().click({ button: 'right' });
  79  |     await expect(page.locator(Hierarchy.contextMenu)).toBeVisible({
  80  |       timeout: 15_000,
  81  |     });
  82  |     const linkItem = page.locator(Hierarchy.contextMenuItem('Show Direct Link'));
  83  |     await linkItem.hover();
  84  |     await linkItem.click();
  85  | 
  86  |     const modal = page.locator(Modal.root).filter({ hasText: 'Direct Link' });
  87  |     await expect(modal).toBeVisible({ timeout: 15_000 });
  88  |     const link = await modal.locator('textarea').inputValue();
  89  |     // The dialog serializes the place as ?fragment=<urlencoded hash token>.
  90  |     expect(link).toMatch(/\?fragment=projects%2F[0-9a-f-]{36}%2Fperspectives%2F/);
  91  |     expect(link).toContain('selection');
  92  |     await page.locator(Modal.primary).click();
  93  |     await expect(modal).toHaveCount(0);
  94  |   });
  95  | 
  96  |   test('NV4: the Home button returns to the project list', async ({
  97  |     page,
  98  |     project,
  99  |   }) => {
  100 |     await page.locator(TopBar.homeButton).click();
  101 |     await expect(page).toHaveURL(/#projects\/list/, { timeout: 15_000 });
  102 |     await expect(page.locator(ProjectList.root)).toBeVisible({ timeout: 15_000 });
  103 | 
  104 |     // Place history integration: back returns into the project.
  105 |     await page.goBack();
  106 |     await expect(page.locator(ProjectView.root)).toBeVisible({ timeout: 30_000 });
  107 |   });
  108 | 
  109 |   test('NV5: the selection survives a perspective switch', async ({
  110 |     page,
  111 |     project,
  112 |   }) => {
  113 |     await page.locator(Hierarchy.treeNode('owl:Thing')).first().click();
  114 |     await page.waitForURL(/\?selection=Class\(owl:Thing\)/, { timeout: 15_000 });
  115 | 
  116 |     await page.locator(ProjectView.tab('History')).click();
  117 |     await page.locator(ProjectView.tab('Classes')).click();
  118 | 
  119 |     await expect(page).toHaveURL(/\?selection=Class\(owl:Thing\)/, {
  120 |       timeout: 15_000,
  121 |     });
  122 |     await expect(
  123 |       page.locator(Hierarchy.selectedNode).filter({ hasText: 'owl:Thing' }),
  124 |     ).toBeVisible({ timeout: 15_000 });
  125 |   });
  126 | });
  127 | 
```