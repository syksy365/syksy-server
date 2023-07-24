"use strict";(self.webpackChunksyksy_web=self.webpackChunksyksy_web||[]).push([[37],{51089:function(e,t,r){r(33209);var n=r(57025),s=r(75782),a=r(58733),i=r(4637),o=r(87441),l=r(59496),c=r(84198),u=r(54021),d=["options","fieldProps","proFieldProps","valueEnum"],p=l.forwardRef((function(e,t){var r=e.options,n=e.fieldProps,l=e.proFieldProps,c=e.valueEnum,p=(0,a.Z)(e,d);return(0,i.jsx)(u.Z,(0,s.Z)({ref:t,valueType:"checkbox",valueEnum:(0,o.h)(c,void 0),fieldProps:(0,s.Z)({options:r},n),lightProps:(0,s.Z)({labelFormatter:function(){return(0,i.jsx)(u.Z,(0,s.Z)({ref:t,valueType:"checkbox",mode:"read",valueEnum:(0,o.h)(c,void 0),filedConfig:{customLightMode:!0},fieldProps:(0,s.Z)({options:r},n),proFieldProps:l},p))}},p.lightProps),proFieldProps:l},p))})),f=l.forwardRef((function(e,t){var r=e.fieldProps,a=e.children;return(0,i.jsx)(n.Z,(0,s.Z)((0,s.Z)({ref:t},r),{},{children:a}))})),h=(0,c.G)(f,{valuePropName:"checked"});h.Group=p,t.Z=h},74958:function(e,t,r){r.d(t,{A:function(){return w}});r(98649);var n=r(35195),s=r(75782),a=r(4637),i=r(29214),o=r(24572),l=(r(34940),r(6825)),c=(r(77178),r(57500)),u=r(91600),d=r(30264),p=r(84749),f=r(39277),h=r(19803),m=r.n(h),v=r(59496),g=r(19090),x=r(23182),j=v.forwardRef((function(e,t){var r=v.useContext(g.Z).groupProps,n=(0,s.Z)((0,s.Z)({},r),e),i=n.children,h=n.collapsible,j=n.defaultCollapsed,Z=n.style,y=n.labelLayout,w=n.title,b=void 0===w?e.label:w,_=n.tooltip,P=n.align,M=void 0===P?"start":P,C=n.direction,N=n.size,k=void 0===N?32:N,F=n.titleStyle,z=n.titleRender,S=n.spaceProps,I=n.extra,L=n.autoFocus,E=(0,p.Z)((function(){return j||!1}),{value:e.collapsed,onChange:e.onCollapse}),R=(0,u.Z)(E,2),V=R[0],T=R[1],q=(0,v.useContext)(c.ZP.ConfigContext).getPrefixCls,D=(0,x.zx)(e),W=D.ColWrapper,B=D.RowWrapper,A=q("pro-form-group"),G=h&&(0,a.jsx)(d.Z,{style:{marginRight:8},rotate:V?void 0:90}),U=(0,a.jsx)(f.Z,{label:G?(0,a.jsxs)("div",{children:[G,b]}):b,tooltip:_}),H=(0,v.useCallback)((function(e){var t=e.children;return(0,a.jsx)(l.Z,(0,s.Z)((0,s.Z)({},S),{},{className:m()("".concat(A,"-container"),null==S?void 0:S.className),size:k,align:M,direction:C,style:(0,s.Z)({rowGap:0},null==S?void 0:S.style),children:t}))}),[M,A,C,k,S]),O=z?z(U,e):U,Y=(0,v.useMemo)((function(){var e=[],t=v.Children.toArray(i).map((function(t,r){var n;return v.isValidElement(t)&&(null==t||null===(n=t.props)||void 0===n?void 0:n.hidden)?(e.push(t),null):0===r&&v.isValidElement(t)&&L?v.cloneElement(t,(0,s.Z)((0,s.Z)({},t.props),{},{autoFocus:L})):t}));return[(0,a.jsx)(B,{Wrapper:H,children:t},"children"),e.length>0?(0,a.jsx)("div",{style:{display:"none"},children:e}):null]}),[i,B,H,L]),Q=(0,u.Z)(Y,2),J=Q[0],K=Q[1];return(0,a.jsx)(W,{children:(0,a.jsxs)("div",{className:m()(A,(0,o.Z)({},"".concat(A,"-twoLine"),"twoLine"===y)),style:Z,ref:t,children:[K,(b||_||I)&&(0,a.jsx)("div",{className:"".concat(A,"-title"),style:F,onClick:function(){T(!V)},children:I?(0,a.jsxs)("div",{style:{display:"flex",width:"100%",alignItems:"center",justifyContent:"space-between"},children:[O,(0,a.jsx)("span",{onClick:function(e){return e.stopPropagation()},children:I})]}):O}),h&&V?null:J]})})}));j.displayName="ProForm-Group";var Z=j,y=r(13026);function w(e){return(0,a.jsx)(i.I,(0,s.Z)({layout:"vertical",submitter:{render:function(e,t){return t.reverse()}},contentRender:function(e,t){return(0,a.jsxs)(a.Fragment,{children:[e,t]})}},e))}w.Group=Z,w.useForm=n.Z.useForm,w.Item=y.Z,w.useWatch=n.Z.useWatch,w.ErrorList=n.Z.ErrorList,w.Provider=n.Z.Provider,w.useFormInstance=n.Z.useFormInstance},39277:function(e,t,r){r.d(t,{Z:function(){return f}});var n=r(75782),s=(r(7085),r(76384)),a=r(24572),i=(r(77178),r(57500)),o=r(4637),l=r(52153),c=r(19803),u=r.n(c),d=r(59496),p=function(e){var t=e.label,r=e.tooltip,c=e.ellipsis,p=e.subTitle,f=(0,d.useContext)(i.ZP.ConfigContext).getPrefixCls;if(!r&&!p)return(0,o.jsx)(o.Fragment,{children:t});var h=f("pro-core-label-tip"),m="string"==typeof r||d.isValidElement(r)?{title:r}:r,v=(null==m?void 0:m.icon)||(0,o.jsx)(l.Z,{});return(0,o.jsxs)("div",{className:h,onMouseDown:function(e){return e.stopPropagation()},onMouseLeave:function(e){return e.stopPropagation()},onMouseMove:function(e){return e.stopPropagation()},children:[(0,o.jsx)("div",{className:u()("".concat(h,"-title"),(0,a.Z)({},"".concat(h,"-title-ellipsis"),c)),children:t}),p&&(0,o.jsx)("div",{className:"".concat(h,"-subtitle"),children:p}),r&&(0,o.jsx)(s.Z,(0,n.Z)((0,n.Z)({},m),{},{children:(0,o.jsx)("span",{className:"".concat(h,"-icon"),children:v})}))]})},f=d.memo(p)},66535:function(e,t,r){r.r(t),r.d(t,{default:function(){return U}});var n=r(25359),s=r.n(n),a=r(57213),i=r.n(a),o=r(49811),l=r.n(o),c=r(54306),u=r.n(c),d=r(43961),p=r(84045),f=r(59496),h={icon:function(e,t){return{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M832 464h-68V240c0-70.7-57.3-128-128-128H388c-70.7 0-128 57.3-128 128v224h-68c-17.7 0-32 14.3-32 32v384c0 17.7 14.3 32 32 32h640c17.7 0 32-14.3 32-32V496c0-17.7-14.3-32-32-32zM332 240c0-30.9 25.1-56 56-56h248c30.9 0 56 25.1 56 56v224H332V240zm460 600H232V536h560v304z",fill:e}},{tag:"path",attrs:{d:"M232 840h560V536H232v304zm280-226a48.01 48.01 0 0128 87v53c0 4.4-3.6 8-8 8h-40c-4.4 0-8-3.6-8-8v-53a48.01 48.01 0 0128-87z",fill:t}},{tag:"path",attrs:{d:"M484 701v53c0 4.4 3.6 8 8 8h40c4.4 0 8-3.6 8-8v-53a48.01 48.01 0 10-56 0z",fill:e}}]}},name:"lock",theme:"twotone"},m=r(38119),v=function(e,t){return f.createElement(m.Z,(0,p.Z)((0,p.Z)({},e),{},{ref:t,icon:h}))};v.displayName="LockTwoTone";var g=f.forwardRef(v),x={icon:{tag:"svg",attrs:{viewBox:"0 0 1024 1024",focusable:"false"},children:[{tag:"path",attrs:{d:"M512 64L128 192v384c0 212.1 171.9 384 384 384s384-171.9 384-384V192L512 64zm312 512c0 172.3-139.7 312-312 312S200 748.3 200 576V246l312-110 312 110v330z"}},{tag:"path",attrs:{d:"M378.4 475.1a35.91 35.91 0 00-50.9 0 35.91 35.91 0 000 50.9l129.4 129.4 2.1 2.1a33.98 33.98 0 0048.1 0L730.6 434a33.98 33.98 0 000-48.1l-2.8-2.8a33.98 33.98 0 00-48.1 0L483 579.7 378.4 475.1z"}}]},name:"safety",theme:"outlined"},j=function(e,t){return f.createElement(m.Z,(0,p.Z)((0,p.Z)({},e),{},{ref:t,icon:x}))};j.displayName="SafetyOutlined";var Z=f.forwardRef(j),y=r(17917),w=r(99325),b=r(72708),_=r(96453),P=r(74958),M=r(6333),C=r(51089),N=r(25389),k=r(67587),F=r(88641);let z=(e=21)=>crypto.getRandomValues(new Uint8Array(e)).reduce(((e,t)=>e+=(t&=63)<36?t.toString(36):t<62?(t-26).toString(36).toUpperCase():t>62?"-":"_"),"");var S="container___InCDr",I="lang___RwYTh",L="content___byzFz",E="form___qrIjX",R="top___aIaTo",V="header___vdxMK",T="logo___CnY9S",q="title___QcvZ9",D="desc___BOds5",W="main___uLpMe",B="prefixIcon___Ug0YF",A=r(4637),G=function(e){var t=e.content;return(0,A.jsx)(y.Z,{style:{marginBottom:24},message:t,type:"error",showIcon:!0})},U=function(){var e=(0,f.useRef)(),t=(0,f.useState)(!1),r=u()(t,2),n=r[0],a=r[1],o=(0,f.useState)(30),c=u()(o,2),p=c[0],h=c[1],m=(0,f.useState)(!1),v=u()(m,2),x=v[0],j=v[1],y=function(t){clearInterval(e.current);var r=t;e.current=setInterval((function(){--r<0&&(j(!0),clearInterval(e.current))}),1e3)};(0,f.useEffect)((function(){(0,F.v3)().then((function(e){a(e.data.enable),h(e.data.effectiveTime),e.data.enable&&y(e.data.effectiveTime)}))}),[]);var U=(0,f.useState)(!1),H=u()(U,2),O=H[0],Y=H[1],Q=(0,f.useState)({}),J=u()(Q,2),K=J[0],X=J[1],$=(0,N.useModel)("@@initialState"),ee=$.initialState,te=$.setInitialState,re=(0,N.useIntl)(),ne=function(){var e=l()(s()().mark((function e(){var t,r,n,a,o,l,c,u;return s()().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return n=null==ee||null===(t=ee.fetchUserInfo)||void 0===t?void 0:t.call(ee),a=null==ee||null===(r=ee.fetchMenuData)||void 0===r?void 0:r.call(ee),e.next=4,n;case 4:return o=e.sent,e.next=7,a;case 7:l=e.sent,c=null==l?void 0:l.menuData,u=null==l?void 0:l.menuOperateData,o&&te(i()(i()({},ee),{},{currentUser:o,menuData:c,menuOperateData:u}));case 11:case"end":return e.stop()}}),e)})));return function(){return e.apply(this,arguments)}}(),se=function(){var t=l()(s()().mark((function t(r){var n;return s()().wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return Y(!0),t.prev=1,t.next=4,(0,F.Qj)(i()({},r));case 4:if("ok"!==(n=t.sent).status){t.next=12;break}return w.ZP.success("登录成功！"),t.next=9,ne();case 9:return clearInterval(e.current),N.history&&setTimeout((function(){var e=N.history.location.redirect;N.history.push(e||"/")}),10),t.abrupt("return");case 12:X(n),t.next=18;break;case 15:t.prev=15,t.t0=t.catch(1),w.ZP.error("登录失败，请重试！");case 18:Y(!1);case 19:case"end":return t.stop()}}),t,null,[[1,15]])})));return function(e){return t.apply(this,arguments)}}(),ae=K.status,ie=(0,f.useState)(z()),oe=u()(ie,2),le=oe[0],ce=oe[1];return(0,A.jsxs)("div",{className:S,children:[(0,A.jsx)("div",{className:I,children:N.SelectLang&&(0,A.jsx)(N.SelectLang,{})}),(0,A.jsx)("div",{className:L,children:(0,A.jsxs)("div",{className:E,children:[(0,A.jsxs)("div",{className:R,children:[(0,A.jsx)("div",{className:V,children:(0,A.jsxs)(N.Link,{to:"/",children:[(0,A.jsx)("img",{alt:"logo",className:T,src:"logo.svg"}),(0,A.jsx)("span",{className:q,children:"轻舟"})]})}),(0,A.jsx)("div",{className:D,children:"为Java Web工程提供基础支撑"})]}),(0,A.jsx)("div",{className:W,children:(0,A.jsxs)(P.A,{initialValues:{autoLogin:!0},submitter:{searchConfig:{submitText:re.formatMessage({id:"pages.login.submit",defaultMessage:"登录"})},render:function(e,t){return t.pop()},submitButtonProps:{loading:O,size:"large",style:{width:"100%"}}},onFinish:function(){var e=l()(s()().mark((function e(t){return s()().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:se(t);case 1:case"end":return e.stop()}}),e)})));return function(t){return e.apply(this,arguments)}}(),children:["error"===ae&&(0,A.jsx)(G,{content:K.message||"登录失败"}),(0,A.jsxs)(A.Fragment,{children:[(0,A.jsx)(M.Z,{name:"username",fieldProps:{size:"large",prefix:(0,A.jsx)(d.Z,{className:B})},placeholder:re.formatMessage({id:"pages.login.username.placeholder",defaultMessage:"用户名: admin"}),rules:[{required:!0,message:(0,A.jsx)(N.FormattedMessage,{id:"pages.login.username.required"})}]}),(0,A.jsx)(M.Z.Password,{name:"password",fieldProps:{size:"large",prefix:(0,A.jsx)(g,{className:B})},placeholder:re.formatMessage({id:"pages.login.password.placeholder"}),rules:[{required:!0,message:(0,A.jsx)(N.FormattedMessage,{id:"pages.login.password.required",defaultMessage:"请输入密码！"})}]}),n?(0,A.jsxs)(b.Z,{gutter:8,children:[(0,A.jsx)(_.Z,{span:13,children:(0,A.jsx)(M.Z,{name:"captcha",fieldProps:{size:"large",prefix:(0,A.jsx)(Z,{className:B})},placeholder:re.formatMessage({id:"pages.login.captcha.placeholder"}),rules:[{required:!0,message:(0,A.jsx)(N.FormattedMessage,{id:"pages.login.captcha.required"})}]})}),(0,A.jsx)(_.Z,{span:11,onClick:function(){ce(z()),y(p),j(!1)},children:(0,A.jsxs)("div",{style:{position:"relative"},children:[(0,A.jsx)("img",{height:"40px",width:"148px",style:{position:"absolute",maxWidth:"100%",height:"auto"},src:"/qz/api/captcha?key="+le},le),(0,A.jsx)("span",{style:{position:"absolute",display:x?"flex":"none",height:"40px",width:"148px",backdropFilter:"blur(6px)",fontSize:"larger",alignItems:"center",textAlign:"center"},children:"已过期，点击刷新"})]})})]}):null]}),(0,A.jsxs)("div",{style:{marginBottom:24},children:[(0,A.jsx)(C.Z,{noStyle:!0,name:"autoLogin",children:(0,A.jsx)(N.FormattedMessage,{id:"pages.login.rememberMe",defaultMessage:"自动登录"})}),(0,A.jsx)("a",{style:{float:"right"},children:(0,A.jsx)(N.FormattedMessage,{id:"pages.login.forgotPassword",defaultMessage:"忘记密码"})})]})]})})]})}),(0,A.jsx)(k.Z,{})]})}}}]);