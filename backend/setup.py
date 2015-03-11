from setuptools import setup
from pip.req import parse_requirements

requirements = [str(ir.req) for ir in parse_requirements("/")]


setup(name='Android Distribution Platform',
      version='1.0',
      description='A basic Flask app with static files',
      author='Dionysis Lorentzos',
      author_email='dionysis@lorentzos.com',
      url='',
      install_requires=requirements,
)
