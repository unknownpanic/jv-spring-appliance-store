const TRANSLATIONS = {
  en: {
    nav: { home:'Home', appliances:'Appliances', orders:'Orders', clients:'Clients', employees:'Employees', manufacturers:'Manufacturers', logout:'Logout', login:'Login', register:'Register' },
    auth: { email:'Email', password:'Password', repeatPassword:'Repeat password', name:'Full Name', phone:'Phone', loginTitle:'Sign in to your account', registerTitle:'Create a new account', noAccount:"Don't have an account?", hasAccount:'Already have an account?', loginBtn:'Sign In', registerBtn:'Create Account', login:'Login', register:'Register', profile:"My profile" },
    common: { id:'ID', name:'Name', email:'Email', actions:'Actions', edit:'Edit', delete:'Delete', create:'Create', save:'Save', cancel:'Cancel', search:'Search', clear:'Clear', loading:'Loading...', noData:'No data found', confirm:'Confirm', yes:'Yes', no:'No', back:'Back', approved:'Approved', pending:'Pending', add:'Add', update:'Update' },
    appliances: { title:'Appliances', add:'Add Appliance', edit:'Edit Appliance', name:'Name', category:'Category', model:'Model', manufacturer:'Manufacturer', powerType:'Power Type', characteristic:'Characteristic', description:'Description', power:'Power (W)', price:'Price', searchPlaceholder:'Search appliances...' },
    orders: { title:'Orders', add:'New Order', edit:'Edit Order', clientId:'Client ID', employeeId:'Employee ID', rows:'Order Items', applianceId:'Appliance ID', quantity:'Quantity', amount:'Amount', addItem:'Add Item', approveOrder:'Approve Order' },
    clients: { title:'Clients', add:'Add Client', edit:'Edit Client', name:'Name', email:'Email', phone:'Phone', card:'Card' },
    employees: { title:'Employees', add:'Add Employee', edit:'Edit Employee', name:'Name', email:'Email', department:'Department' },
    manufacturers: { title:'Manufacturers', add:'Add Manufacturer', edit:'Edit Manufacturer', name:'Name' },
    home: { subtitle:'Appliance store management system', signInMsg:'Sign in or register to continue', registerNote:'Registration is available for clients only' },
    errors: { notFound:'Page Not Found', notFoundDesc:'The page you are looking for does not exist.', forbidden:'Access Forbidden', forbiddenDesc:'You do not have permission to view this page.', goHome:'Go to Home' },
    sort: { default: "Default", priceAsc: "Price: Low to High", priceDesc: "Price: High to Low", catAsc: "Category: A-Z", catDesc: "Category: Z-A"},
  },
  uk: {
    nav: { home:'Головна', appliances:'Техніка', orders:'Замовлення', clients:'Клієнти', employees:'Працівники', manufacturers:'Виробники', logout:'Вийти', login:'Увійти', register:'Реєстрація' },
    auth: { email:'Email', password:'Пароль', repeatPassword:'Повторіть пароль', name:"Повне ім'я", phone:'Телефон', loginTitle:'Увійдіть до свого акаунту', registerTitle:'Створіть новий акаунт', noAccount:'Немає акаунту?', hasAccount:'Вже є акаунт?', loginBtn:'Увійти', registerBtn:'Зареєструватися', login:'Увійти', register:'Реєстрація', profile:"Мій профіль" },
    common: { id:'ID', name:'Назва', email:'Email', actions:'Дії', edit:'Редагувати', delete:'Видалити', create:'Створити', save:'Зберегти', cancel:'Скасувати', search:'Пошук', clear:'Очистити', loading:'Завантаження...', noData:'Даних не знайдено', confirm:'Підтвердити', yes:'Так', no:'Ні', back:'Назад', approved:'Затверджено', pending:'Очікує', add:'Додати', update:'Оновити' },
    appliances: { title:'Техніка', add:'Додати техніку', edit:'Редагувати техніку', name:'Назва', category:'Категорія', model:'Модель', manufacturer:'Виробник', powerType:'Тип живлення', characteristic:'Характеристики', description:'Опис', power:'Потужність (Вт)', price:'Ціна', searchPlaceholder:'Пошук техніки...' },
    orders: { title:'Замовлення', add:'Нове замовлення', edit:'Редагувати замовлення', clientId:'ID Клієнта', employeeId:'ID Працівника', rows:'Позиції замовлення', applianceId:'ID Техніки', quantity:'Кількість', amount:'Сума', addItem:'Додати позицію', approveOrder:'Затвердити замовлення' },
    clients: { title:'Клієнти', add:'Додати клієнта', edit:'Редагувати клієнта', name:"Ім'я", email:'Email', phone:'Телефон', card:'Банківська картка' },
    employees: { title:'Працівники', add:'Додати працівника', edit:'Редагувати працівника', name:"Ім'я", email:'Email', department:'Відділ' },
    manufacturers: { title:'Виробники', add:'Додати виробника', edit:'Редагувати виробника', name:'Назва' },
    home: { subtitle:'Система управління магазином побутової техніки', signInMsg:'Увійдіть або зареєструйтесь щоб продовжити', registerNote:'Реєстрація доступна лише для клієнтів' },
    errors: { notFound:'Сторінку не знайдено', notFoundDesc:'Сторінка, яку ви шукаєте, не існує.', forbidden:'Доступ заборонено', forbiddenDesc:'У вас немає дозволу для перегляду цієї сторінки.', goHome:'На головну' },
    sort: { default: "За замовчуванням", priceAsc: "Ціна: Від дешевих", priceDesc: "Ціна: Від дорогих", catAsc: "Категорія: А-Я", catDesc: "Категорія: Я-А"},
  }
};

function getLang() { return localStorage.getItem('app_lang') || 'uk'; }
function setLang(l) { localStorage.setItem('app_lang', l); }

function t(key) {
  const keys = key.split('.');
  let val = TRANSLATIONS[getLang()] || TRANSLATIONS['en'];
  for (const k of keys) val = val?.[k];
  return val || key;
}

function applyLang() {
  const lang = getLang();
  document.querySelectorAll('[data-i18n]').forEach(el => {
    const key = el.getAttribute('data-i18n');
    el.textContent = t(key);
  });
  document.querySelectorAll('[data-i18n-placeholder]').forEach(el => {
    el.placeholder = t(el.getAttribute('data-i18n-placeholder'));
  });
  const btn = document.getElementById('langToggle');
  if (btn) btn.textContent = lang === 'uk' ? 'EN' : 'УК';
  document.documentElement.lang = lang;
}

function initLang() {
  applyLang();
  const btn = document.getElementById('langToggle');
  if (btn) {
    btn.addEventListener('click', () => {
      setLang(getLang() === 'uk' ? 'en' : 'uk');
      location.reload();
    });
  }
}
