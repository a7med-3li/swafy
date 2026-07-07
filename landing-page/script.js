function switchAudience(type) {
  document.querySelectorAll('.audience').forEach(function(el) {
    el.classList.remove('active');
  });
  document.querySelectorAll('.audience-tab').forEach(function(el) {
    el.classList.remove('active');
  });

  var panel = document.getElementById('audience-' + type);
  var tab = document.querySelector('[data-audience="' + type + '"]');
  if (panel) panel.classList.add('active');
  if (tab) tab.classList.add('active');

  var target = document.getElementById('audience-' + type);
  if (target) {
    target.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  setTimeout(revealBenefits, 350);
}

function revealBenefits() {
  document.querySelectorAll('.audience.active .benefits li').forEach(function(li, i) {
    setTimeout(function() {
      li.classList.add('in-view');
    }, i * 120);
  });
}

var observer = new IntersectionObserver(function(entries) {
  entries.forEach(function(entry) {
    if (entry.isIntersecting) revealBenefits();
  });
}, { threshold: 0.15 });

document.addEventListener('DOMContentLoaded', function() {
  var funnel = document.querySelector('.audience.active');
  if (funnel) observer.observe(funnel);

  document.querySelectorAll('.audience').forEach(function(el) {
    observer.observe(el);
  });
});

function submitWaitlist(e) {
  e.preventDefault();
  var form = e.target;
  var type = form.getAttribute('data-type');
  var wrap = form.parentElement;
  var success = wrap.parentElement.querySelector('.form-success');

  if (wrap) wrap.style.display = 'none';
  if (success) success.classList.add('show');
  return false;
}

function submitFeedback(e) {
  e.preventDefault();
  var form = e.target;
  var wrap = form.parentElement;
  var success = wrap.parentElement.querySelector('.form-success');

  if (wrap) wrap.style.display = 'none';
  if (success) success.classList.add('show');
  return false;
}
